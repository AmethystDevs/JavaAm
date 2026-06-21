# Publishing the JavaAm VS Code Extension

## One-Time Setup

### 1. Create VS Code Marketplace Account

1. Visit https://marketplace.visualstudio.com/
2. Sign in with Microsoft account (or create one)
3. Click "Publish extensions" at the bottom

### 2. Create Publisher Profile

```bash
# Install vsce
npm install -g @vscode/vsce

# Login to create publisher
vsce create-publisher amethystdevs

# Follow prompts to create publisher account
```

### 3. Generate Personal Access Token (PAT)

1. Go to https://dev.azure.com/_usersSettings/tokens
2. Click "New Token"
3. Set:
   - **Name**: "VS Code Extension Publish"
   - **Organization**: "All accessible organizations"
   - **Scopes**: Check "Marketplace (Publish)"
4. Copy the token and save securely

---

## Publishing Process

### Step 1: Update Version

Edit `package.json`:
```json
{
  "version": "1.0.1"
}
```

### Step 2: Update CHANGELOG

Edit `CHANGELOG.md`:
```markdown
## [1.0.1] - 2026-06-20
- Fixed Java main class detection
- Improved environment variable support
- Better error messages
```

### Step 3: Commit and Tag

```bash
git add package.json CHANGELOG.md
git commit -m "Release v1.0.1"
git tag v1.0.1
git push origin main v1.0.1
```

### Step 4: Publish to Marketplace

#### Option A: Using npm scripts

```bash
cd javaam-vscode

# Set your PAT token as environment variable
export VSCODE_PAT_TOKEN=your-token-here

# Publish with auto-increment
npm run publish-patch    # 1.0.0 → 1.0.1
npm run publish-minor    # 1.0.0 → 1.1.0
npm run publish-major    # 1.0.0 → 2.0.0

# Or publish exact version
npm run publish
```

#### Option B: Manual with vsce

```bash
cd javaam-vscode

# Package
vsce package

# Publish
vsce publish -p YOUR_PAT_TOKEN
```

### Step 5: Verify

1. Wait 2-5 minutes for indexing
2. Visit https://marketplace.visualstudio.com/items?itemName=amethystdevs.javaam-vscode
3. Check version matches

---

## Troubleshooting

### "401 Unauthorized"
- PAT token is invalid or expired
- Generate new token from https://dev.azure.com/_usersSettings/tokens

### "Publishing failed: 403"
- Publisher name doesn't match (must be `amethystdevs`)
- Check `package.json` → `publisher` field

### "Extension not found in Marketplace"
- Still indexing (wait 5-10 minutes)
- Check marketplace: https://marketplace.visualstudio.com/search?term=javaam

### "vsce not found"
```bash
npm install -g @vscode/vsce
```

---

## Creating GitHub Release

After publishing to VS Code Marketplace, create a GitHub release:

```bash
# From project root
gh release create v1.0.1 \
  --title "JavaAm Extension v1.0.1" \
  --notes "See CHANGELOG.md for details"

# Or create via GitHub web UI:
# https://github.com/amethystdevs/javaam/releases/new
```

---

## Full Release Checklist

- [ ] Update `javaam-vscode/package.json` version
- [ ] Update `javaam-vscode/CHANGELOG.md`
- [ ] Update `compiler/pom.xml` version (optional)
- [ ] Commit changes
- [ ] Create git tag: `git tag v1.0.1`
- [ ] Push tag: `git push origin v1.0.1`
- [ ] Run `npm run publish-patch` in `javaam-vscode/`
- [ ] Verify on Marketplace (2-5 min wait)
- [ ] Create GitHub release
- [ ] Announce release (Twitter, Reddit, etc.)

---

## Pre-release Testing

Before publishing:

```bash
# Package locally to test
vsce package

# This creates: javaam-vscode-1.0.1.vsix

# Install locally in VS Code
code --install-extension javaam-vscode-1.0.1.vsix

# Test in VS Code:
# 1. Open a .jvm file
# 2. Run: JavaAm: Compile Current File (Ctrl+Shift+P)
# 3. Verify it works
```

---

## Continuous Publishing (GitHub Actions)

Automatically publish when you create a git tag:

Create `.github/workflows/publish.yml`:

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
      
      - name: Install vsce
        run: npm install -g @vscode/vsce
      
      - name: Publish to Marketplace
        run: vsce publish -p ${{ secrets.VSCODE_PAT_TOKEN }}
        working-directory: javaam-vscode
      
      - name: Create GitHub Release
        uses: softprops/action-gh-release@v1
        with:
          body: |
            Published to VS Code Marketplace
            https://marketplace.visualstudio.com/items?itemName=amethystdevs.javaam-vscode
```

Then set `VSCODE_PAT_TOKEN` secret in GitHub repo settings:
1. Go to Settings → Secrets → New repository secret
2. Name: `VSCODE_PAT_TOKEN`
3. Value: Your PAT token
4. Click "Add secret"

Now just:
```bash
git tag v1.0.1
git push origin v1.0.1
# GitHub Actions automatically publishes!
```

---

## Resources

- **vsce Documentation**: https://code.visualstudio.com/api/working-with-extensions/publishing-extension
- **VS Code Marketplace**: https://marketplace.visualstudio.com/
- **PAT Token Setup**: https://dev.azure.com/_usersSettings/tokens
- **Extension Guidelines**: https://code.visualstudio.com/api/references/extension-guidelines
