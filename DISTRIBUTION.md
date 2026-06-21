# JavaAm Publishing & Distribution Guide

This comprehensive guide covers all methods to publish and distribute JavaAm to end users.

---

## Summary

You have **3 main distribution channels**:

1. **VS Code Marketplace** (Easiest for IDE users) ⭐ Recommended
2. **GitHub Releases** (DIY installers)
3. **Package Managers** (Advanced)

---

## Channel 1: VS Code Marketplace (Recommended)

### Why?
- Millions of VS Code users
- One-click install
- Automatic updates
- Official distribution method

### Process

**Setup (One-time):**

```bash
# 1. Create account on https://marketplace.visualstudio.com/
# 2. Create publisher: https://dev.azure.com/_usersSettings/tokens
# 3. Generate PAT token (Marketplace "Publish" scope)

npm install -g @vsce/vsce
vsce create-publisher amethystdevs
```

**Publish New Version:**

```bash
cd javaam-vscode

# Update version
# Edit package.json: "version": "1.0.1"

# Update changelog
# Edit CHANGELOG.md

# Commit
git add package.json CHANGELOG.md
git commit -m "Release v1.0.1"

# Publish (easiest with npm script)
npm run publish-patch
# Enter PAT token when prompted
```

**Check Result:**
- Wait 2-5 minutes
- Visit: https://marketplace.visualstudio.com/items?itemName=amethystdevs.javaam-vscode
- Users can now install from VS Code

**Update Instructions:**
```
1. Open VS Code
2. Extensions (Ctrl+Shift+X)
3. Search "JavaAm"
4. Click Install
```

---

## Channel 2: GitHub Releases (Portable Downloads)

### Why?
- Full control over distribution
- Users who prefer standalone installers
- Works without VS Code

### What to Release

```
javaam-1.0.1.zip (Portable ZIP)
├── javaam-compiler.jar
├── javaam.bat (Windows launcher)
├── javaam (Unix/Linux/macOS launcher)
├── INSTALL.md
├── examples/
├── docs/
└── README.md
```

### Process

**1. Build Release Package**

```bash
PowerShell -ExecutionPolicy Bypass -File scripts/build-release.ps1 -Version "1.0.1"

# Creates: release/javaam-1.0.1.zip
```

**2. Create GitHub Release**

```bash
# Tag the release
git tag v1.0.1
git push origin v1.0.1

# Create release on GitHub (web UI):
# https://github.com/amethystdevs/javaam/releases/new
# 
# Title: JavaAm v1.0.1
# Description: Paste CHANGELOG.md content
# Attachments: Upload javaam-1.0.1.zip
```

**Download Instructions:**
```
1. Visit: https://github.com/amethystdevs/javaam/releases/
2. Click latest release
3. Download javaam-1.0.1.zip
4. Extract anywhere
5. Add folder to PATH
6. Run: javaam compile file.jvm
```

---

## Channel 3: Windows Installer (.exe)

### Why?
- Professional one-click install
- Windows users expect this
- Handles PATH setup automatically

### Requirements
- NSIS: https://nsis.sourceforge.io/Download

### Process

**1. Build Installer**

```bash
# Install NSIS first

makensis installer/javaam-installer.nsi

# Creates: installer/javaam-1.0.1-installer.exe
```

**2. Release on GitHub**

Same as Channel 2, but upload `.exe` instead of `.zip`

**Installation:**
```
1. Download javaam-1.0.1-installer.exe
2. Double-click to run
3. Follow prompts
4. Installer adds to PATH automatically
5. Done!
```

---

## Channel 4: Package Managers (Advanced)

### Homebrew (macOS/Linux)

Create `homebrew-javaam` repository with formula:

```ruby
# Formula/javaam.rb
class Javaam < Formula
  desc "Simplified Java language for Minecraft mods"
  homepage "https://github.com/amethystdevs/javaam"
  url "https://github.com/amethystdevs/javaam/releases/download/v1.0.1/javaam-1.0.1.zip"
  sha256 "abc123..." # Get from javaam-1.0.1.zip.sha256
  
  depends_on "openjdk@11"
  
  def install
    libexec.install Dir["*"]
    bin.install_symlink "#{libexec}/javaam"
  end
end
```

Installation:
```bash
brew tap amethystdevs/javaam
brew install javaam
```

### Scoop (Windows)

Create entry in Scoop manifest.

### Chocolatey (Windows)

Create Chocolatey package.

---

## Complete Release Checklist

### Before Release

- [ ] All tests pass: `cd compiler && mvn test`
- [ ] Compiler builds: `cd compiler && mvn clean package`
- [ ] Extension loads in VS Code (F5 debug)
- [ ] Documentation is up to date
- [ ] CHANGELOG.md is updated

### Version Updates

- [ ] Update `javaam-vscode/package.json` version
- [ ] Update `javaam-vscode/CHANGELOG.md`
- [ ] Update `compiler/pom.xml` version (optional)
- [ ] Update `README.md` version if mentioned

### Commit & Tag

- [ ] Commit changes: `git commit -m "Release v1.0.1"`
- [ ] Create tag: `git tag v1.0.1`
- [ ] Push: `git push origin main v1.0.1`

### Publish to VS Code

- [ ] CD to `javaam-vscode/`
- [ ] Run: `npm run publish-patch` (or minor/major)
- [ ] Verify on Marketplace (wait 2-5 min)

### Create GitHub Release

- [ ] Go to https://github.com/amethystdevs/javaam/releases/new
- [ ] Title: "JavaAm v1.0.1"
- [ ] Description: Paste CHANGELOG
- [ ] Upload: `javaam-1.0.1.zip` (or `.exe`)

### Optional: Create Installers

- [ ] Build ZIP: `PowerShell -File scripts/build-release.ps1 -Version "1.0.1"`
- [ ] Build EXE: `makensis installer/javaam-installer.nsi`
- [ ] Upload to GitHub Release

### Announce

- [ ] Post to GitHub Discussions
- [ ] Tweet release announcement
- [ ] Post to r/java or r/minecraft if relevant

---

## Automation (GitHub Actions)

Automatically publish when you create a tag:

**.github/workflows/publish-extension.yml:**

```yaml
name: Publish Extension

on:
  push:
    tags:
      - 'v*'

jobs:
  publish:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      
      - name: Publish to Marketplace
        run: |
          npm install -g @vscode/vsce
          cd javaam-vscode
          vsce publish -p ${{ secrets.VSCODE_PAT_TOKEN }}
```

Setup:
1. Go to GitHub repo → Settings → Secrets
2. Add: `VSCODE_PAT_TOKEN` = your PAT token
3. Now just: `git tag v1.0.1 && git push origin v1.0.1` ✨

---

## Distribution Summary Table

| Method | Setup | Ease | Users | Updates | Best For |
|--------|-------|------|-------|---------|----------|
| VS Code Marketplace | 15 min | ⭐⭐⭐ | Millions | Auto | Most users |
| GitHub Releases | 5 min | ⭐⭐⭐ | Tech users | Manual | Downloads |
| Windows .exe | 20 min | ⭐⭐ | Windows users | Manual | Non-tech |
| Homebrew | 30 min | ⭐⭐ | Mac users | Manual | Mac devs |
| npm | 10 min | ⭐⭐⭐ | JS devs | Auto | Package managers |

---

## Next Steps

1. **Publish Extension Now:**
   ```bash
   cd javaam-vscode
   npm run publish-patch
   ```

2. **Create GitHub Release:**
   - Commit & tag: `git tag v1.0.1 && git push origin v1.0.1`
   - Create release on GitHub web UI

3. **Set Up Automation:**
   - Create `.github/workflows/publish-extension.yml`
   - Add `VSCODE_PAT_TOKEN` secret
   - Future: just push tags and CI handles it!

4. **Consider Installers:**
   - Windows: Build NSIS `.exe` installer
   - Mac: Create Homebrew formula
   - Linux: Package for apt/yum

---

## Troubleshooting

**"vsce publish: 401 Unauthorized"**
- PAT token expired
- Generate new: https://dev.azure.com/_usersSettings/tokens

**"Extension not showing in Marketplace"**
- Wait 5-10 minutes for indexing
- Check: https://marketplace.visualstudio.com/search?term=javaam

**"makensis not found"**
- Install NSIS: https://nsis.sourceforge.io/Download

**"Java not found for builds"**
- Install Java 11+: https://www.oracle.com/java/technologies/downloads/
- Set `JAVA_HOME` environment variable

---

## Support & Questions

- **GitHub Discussions**: https://github.com/amethystdevs/javaam/discussions
- **Issues**: https://github.com/amethystdevs/javaam/issues
- **VS Code Ext Docs**: https://code.visualstudio.com/api/working-with-extensions/publishing-extension
