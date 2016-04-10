
# Convert Bzr branch to Git

**Convert bzr branch to git** is an tiny program for transfer source code from bazaar branch to git system with keep all commit information
Currently, it just test on
OSX El Capitan v10.11
JDK 8
git --version: 2.7.0
bzr --version: 2.6.0

Test one my local: max 1.5s = 1 commit transfer

----------

## Preparing

- Bazaar command installed
- Git command installed
- How to check installed: run terminal and type bzr or git to check
- JDK 8 installed
- Java IDE (IntelliJ IDE)

-------------
- Url bazaar branch and permission to checkout
- Git repository
- Clone/Create git repo on local. Ex: folder A
- Setup branch on git for transfer
- Update .gitignore to ignore bzr:
```python
*.bzr
*.bzrignore
```
- Use bzr checkout branch at anywhere you like: bzr checkout [location]
- After checkout, copy folder included .bzr to git folder (folder A above). Look like bzr inside git.
- Use bzr get all log revision: bzr log > log-save
- Update contend log save to project
- Update info path bzr (folder included .bzr) and path to git (folder include .git) in config.json in project
- Unbind bzr to host for offline working and fastest running: bzr unbind
Everything ok now

----------------

## Running
Option 1: Open project with IDE, run, view log
Option 2: Run jar file with command

Run with custom config file and commit data file path
```java
java -jar bzr-git-auto.jar config.json commit-data
```

Run with no custom path file
```java
java -jar bzr-git-auto.jar
```

## Utils command
### Git

https://confluence.atlassian.com/bitbucketserver/basic-git-commands-776639767.html
https://www.atlassian.com/git/tutorials/setting-up-a-repository/git-init

git add -A
git commit -m "content"
git push origin -[local branch]:[remote branch]
git checkout [branch/filename]
git status
git diff

git clone -b [branch] [remote_repo]
ex: git clone -b branches/2.22 https://bitbucket.org/hispin/dhis-india-bzr
Clone the repository
git clone [repository_url]

List all branches
git branch -a

Checkout the branch that you want
git checkout [name_of_branch]
git checkout -b [branch-name] [origin/branch_name]

View log (from HEAD to begin time)
git log >test
```java
git log item:
commit eda1c5ad35e0fa84eeda470678168c050a5e0a5c
Author: Nhan Cao <nhancv1992@gmail.com>
Date:   Thu Apr 7 20:07:09 2016 +0530

    message in here

```
git checkout [sha1]
ex:
git checkout eda1c5ad35e0fa84eeda470678168c050a5e0a5c

### Bazaar
Ssh:
https://help.launchpad.net/YourAccount/CreatingAnSSHKeyPair
https://launchpad.net/~/+editsshkeys

Checkout: When get all latest source code it auto bind this remote. You have to unbind after that
bzr checkout lp:~dhis2-devs-core/dhis2/test

Fetch data: Juts get all latest source code and NOT auto bind
bzr branch lp:~dhis2-devs-core/dhis2/test

Pull data:
bzr pull lp:~dhis2-devs-core/dhis2/test

Push data:
bzr push lp:~dhis2-devs-core/dhis2/test

Bind or unbind remote:
bzr bind lp:~dhis2-devs-core/dhis2/test
bzr unbind

Other:
If you had checkout first or bind to remote you can use bzr up to update on remote, otherwise you update on local.
Update: bzr up
Update to revision: bzr up -r [revision]
ex: bzr up -r 1
Add all file to staged: bzr add *
View diff infomation with prev commit: bzr diff
View change status of all current files: bzr status
Commit: If you set bind to remote, when you run commit, it auto commit to remote. Make sure unbind first for carefully.
bzr commit -m "extend api file resource"
View current revision number: bzr revno
Help: bzr help


## Feedback & Bug Report
- Email: <nhancv1992@gmail.com>


