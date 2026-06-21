const vscode = require('vscode');
const cp = require('child_process');
const fs = require('fs');
const path = require('path');

function activate(context) {
    const output = vscode.window.createOutputChannel('JavaAm');

    const compile = vscode.commands.registerCommand('javaam.compile', async () => {
        const editor = vscode.window.activeTextEditor;
        if (!editor) {
            return vscode.window.showInformationMessage('Open a JavaAm file before compiling.');
        }

        if (editor.document.isDirty) {
            await editor.document.save();
        }

        const filePath = editor.document.uri.fsPath;
        const workspace = getWorkspaceRoot();
        if (!workspace) {
            return vscode.window.showErrorMessage('JavaAm workspace not found.');
        }

        const compilerDir = path.join(workspace, 'compiler');
        if (!fs.existsSync(path.join(compilerDir, 'pom.xml'))) {
            return vscode.window.showErrorMessage('JavaAm compiler folder not found in workspace. Ensure the workspace contains compiler/pom.xml.');
        }
        try {
            const command = buildJavaAmCommand(`compile ${quoteShell(filePath)}`, compilerDir, workspace);
            runShellCommand(command, compilerDir, output);
        } catch (err) {
            vscode.window.showErrorMessage(err.message);
        }
    });

    const launchDev = vscode.commands.registerCommand('javaam.launchDev', async () => {
        const workspace = getWorkspaceRoot();
        if (!workspace) {
            return vscode.window.showErrorMessage('JavaAm workspace not found.');
        }

        const option = await vscode.window.showQuickPick([
            'Open JVM mcmp dev help',
            'Create project from template',
            'Generate JavaAm file with AI prompt',
            'Upload local file to workspace'
        ], { placeHolder: 'Launch JVM mcmp dev' });

        if (!option) {
            return;
        }

            const compilerDir = path.join(workspace, 'compiler');
        if (!fs.existsSync(path.join(compilerDir, 'pom.xml'))) {
            return vscode.window.showErrorMessage('JavaAm compiler folder not found in workspace. Ensure the workspace contains compiler/pom.xml.');
        }
        if (option === 'Open JVM mcmp dev help') {
            try {
                runShellCommand(buildJavaAmCommand('dev help', compilerDir, workspace), compilerDir, output);
            } catch (err) {
                vscode.window.showErrorMessage(err.message);
            }
            return;
        }

        if (option === 'Create project from template') {
            await createTemplateProjectWorkspace(workspace, compilerDir, output);
            return;
        }

        if (option === 'Generate JavaAm file with AI prompt') {
            const prompt = await vscode.window.showInputBox({ prompt: 'Describe the mod or plugin to generate' });
            if (!prompt) return;
            const outputFile = await vscode.window.showInputBox({ prompt: 'Output file path', value: path.join(workspace, 'generated.jvm') });
            if (!outputFile) return;
            try {
                runShellCommand(buildJavaAmCommand(`dev ai ${quoteShell(prompt)} ${quoteShell(outputFile)}`, compilerDir, workspace), compilerDir, output);
            } catch (err) {
                vscode.window.showErrorMessage(err.message);
            }
            return;
        }
        if (option === 'Upload local file to workspace') {
            const source = await vscode.window.showInputBox({ prompt: 'Source file path' });
            if (!source) return;
            const target = await vscode.window.showInputBox({ prompt: 'Target folder', value: workspace });
            try {
                runShellCommand(buildJavaAmCommand(`dev upload ${quoteShell(source)} ${quoteShell(target)}`, compilerDir, workspace), compilerDir, output);
            } catch (err) {
                vscode.window.showErrorMessage(err.message);
            }
            return;
        }
    });

    const createProject = vscode.commands.registerCommand('javaam.createTemplateProject', async () => {
        const workspace = getWorkspaceRoot();
        if (!workspace) {
            return vscode.window.showErrorMessage('JavaAm workspace not found.');
        }
        const compilerDir = path.join(workspace, 'compiler');
        await createTemplateProjectWorkspace(workspace, compilerDir, output);
    });

    context.subscriptions.push(compile, launchDev, createProject, output);
}

function deactivate() {}

function quoteShell(value) {
    return '"' + value.replace(/(["\\$`])/g, '\\$1') + '"';
}

function getWorkspaceRoot() {
    const folders = vscode.workspace.workspaceFolders;
    if (folders && folders.length > 0) {
        for (const folder of folders) {
            const candidate = folder.uri.fsPath;
            if (fs.existsSync(path.join(candidate, 'compiler', 'pom.xml'))) {
                return candidate;
            }
        }
        // fallback to first workspace folder if no compiler is found
        return folders[0].uri.fsPath;
    }
    return null;
}

function resolveExecutable(name) {
    const finder = process.platform === 'win32' ? 'where' : 'which';
    try {
        const result = cp.execSync(`${finder} ${name}`, { stdio: ['ignore', 'pipe', 'ignore'], shell: true });
        const match = result.toString().trim().split(/\r?\n/)[0];
        return match || null;
    } catch (e) {
        return null;
    }
}

function resolveFromEnvironment(name) {
    const isWindows = process.platform === 'win32';
    const pathParts = [];
    if (name === 'java' || name === 'javac') {
        const javaHome = process.env.JAVA_HOME || process.env.JDK_HOME;
        if (javaHome) {
            const executable = isWindows ? `${name}.exe` : name;
            pathParts.push(path.join(javaHome, 'bin', executable));
        }
    }
    if (name === 'mvn') {
        const mavenHome = process.env.MAVEN_HOME || process.env.M2_HOME;
        if (mavenHome) {
            const executable = isWindows ? 'mvn.cmd' : 'mvn';
            pathParts.push(path.join(mavenHome, 'bin', executable));
        }
    }
    for (const candidate of pathParts) {
        if (fs.existsSync(candidate)) {
            return candidate;
        }
    }
    return null;
}

function resolveMavenCommand(workspaceRoot, compilerDir) {
    const wrapperPaths = [
        path.join(compilerDir, 'mvnw.cmd'),
        path.join(compilerDir, 'mvnw'),
        path.join(workspaceRoot, 'mvnw.cmd'),
        path.join(workspaceRoot, 'mvnw')
    ];

    for (const wrapperPath of wrapperPaths) {
        if (fs.existsSync(wrapperPath)) {
            return quoteShell(wrapperPath);
        }
    }

    const mvn = resolveExecutable('mvn') || resolveFromEnvironment('mvn');
    return mvn ? quoteShell(mvn) : null;
}

function resolveJavaCommand() {
    const java = resolveExecutable('java') || resolveFromEnvironment('java');
    return java ? quoteShell(java) : null;
}

function resolveJavacCommand() {
    const javac = resolveExecutable('javac') || resolveFromEnvironment('javac');
    return javac ? quoteShell(javac) : null;
}

function collectJavaSources(srcDir) {
    let sources = [];
    if (!fs.existsSync(srcDir)) {
        return sources;
    }
    for (const entry of fs.readdirSync(srcDir, { withFileTypes: true })) {
        const entryPath = path.join(srcDir, entry.name);
        if (entry.isDirectory()) {
            sources = sources.concat(collectJavaSources(entryPath));
        } else if (entry.isFile() && entry.name.endsWith('.java')) {
            sources.push(entryPath);
        }
    }
    return sources;
}

function detectMainClassName(classesPath) {
    const candidates = [
        { file: path.join(classesPath, 'com', 'javaam', 'Main.class'), name: 'com.javaam.Main' },
        { file: path.join(classesPath, 'net', 'imp', 'javaam', 'Main.class'), name: 'net.imp.javaam.Main' }
    ];
    for (const candidate of candidates) {
        if (fs.existsSync(candidate.file)) {
            return candidate.name;
        }
    }
    return null;
}

function detectMainClassNameFromSource(compilerDir) {
    const mainSource = path.join(compilerDir, 'src', 'main', 'java', 'net', 'imp', 'javaam', 'Main.java');
    if (!fs.existsSync(mainSource)) {
        return null;
    }
    const source = fs.readFileSync(mainSource, 'utf8');
    const match = source.match(/^[ \t]*package\s+([a-zA-Z0-9_.]+)\s*;/m);
    if (!match) {
        return null;
    }
    return `${match[1]}.Main`;
}

function buildJavaAmCommand(args, compilerDir, workspaceRoot) {
    const javaCmd = resolveJavaCommand();
    if (!javaCmd) {
        throw new Error('Java not found. Install Java 11+ and make sure java is on your PATH or set JAVA_HOME.');
    }

    const classesPath = path.join(compilerDir, 'target', 'classes');
    const existingMainClass = detectMainClassName(classesPath);
    const sourceMainClass = detectMainClassNameFromSource(compilerDir);
    const mainClass = existingMainClass || sourceMainClass || 'com.javaam.Main';

    const mavenCmd = resolveMavenCommand(workspaceRoot, compilerDir);
    if (mavenCmd) {
        return `${mavenCmd} -q compile && ${javaCmd} -cp target/classes ${mainClass} ${args}`;
    }

    if (existingMainClass) {
        return `${javaCmd} -cp ${quoteShell(classesPath)} ${existingMainClass} ${args}`;
    }

    const jarPath = path.join(compilerDir, 'target', 'javaam-compiler-1.0.0.jar');
    if (fs.existsSync(jarPath)) {
        return `${javaCmd} -jar ${quoteShell(jarPath)} ${args}`;
    }

    const javacCmd = resolveJavacCommand();
    if (!javacCmd) {
        throw new Error('Maven not found and javac is unavailable. Install Maven or a JDK, then reopen VS Code.');
    }

    const outputDir = path.join(compilerDir, 'target', 'classes');
    if (!fs.existsSync(outputDir)) {
        fs.mkdirSync(outputDir, { recursive: true });
    }

    const srcRoot = path.join(compilerDir, 'src', 'main', 'java');
    const sources = collectJavaSources(srcRoot);
    if (sources.length === 0) {
        throw new Error('Unable to find JavaAm compiler source files under compiler/src/main/java.');
    }

    const sourceFiles = sources.map(quoteShell).join(' ');
    return `${javacCmd} -d ${quoteShell(outputDir)} ${sourceFiles} && ${javaCmd} -cp ${quoteShell(outputDir)} ${mainClass} ${args}`;
}

function runShellCommand(command, cwd, output) {
    output.show(true);
    output.appendLine(`> ${command}`);
    const proc = cp.exec(command, { cwd, shell: true });
    proc.stdout.on('data', data => output.append(data.toString()));
    proc.stderr.on('data', data => output.append(data.toString()));

    proc.on('close', code => {
        output.appendLine(`\nProcess exited with code ${code}`);
        if (code === 0) {
            vscode.window.showInformationMessage('JavaAm command completed successfully.');
        } else {
            vscode.window.showErrorMessage('JavaAm command failed. See output for details.');
        }
    });
}

async function createTemplateProjectWorkspace(workspace, compilerDir, output) {
    const templates = getAvailableTemplates(workspace);
    if (templates.length === 0) {
        return vscode.window.showErrorMessage('No templates found in the workspace templates/ folder.');
    }

    const selection = await vscode.window.showQuickPick(templates, { placeHolder: 'Choose a JavaAm template' });
    if (!selection) {
        return;
    }

    const target = await vscode.window.showInputBox({
        prompt: 'Target folder for the new project',
        value: path.join(workspace, `${selection}-project`)
    });
    if (!target) {
        return;
    }

    try {
        runShellCommand(buildJavaAmCommand(`dev init ${quoteShell(selection)} ${quoteShell(target)}`, compilerDir, workspace), compilerDir, output);
    } catch (err) {
        vscode.window.showErrorMessage(err.message);
    }
}

function getAvailableTemplates(workspace) {
    const templatesFolder = path.join(workspace, 'templates');
    if (!fs.existsSync(templatesFolder)) {
        return [];
    }
    try {
        return fs.readdirSync(templatesFolder).filter(name => fs.statSync(path.join(templatesFolder, name)).isDirectory());
    } catch (e) {
        return [];
    }
}

module.exports = {
    activate,
    deactivate
};
