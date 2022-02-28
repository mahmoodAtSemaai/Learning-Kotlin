# Git Guidelines
Yo Developers. 

Please read and follow these guidelines to make our development space more clean and maintainable.

TLDR; //loom video

### Branch rules
1. **Master** branch will be the release/production/deployment branch. Only admins will have the access

2. **Development branch** The development branch is the default branch of the source repository. Any new addition in terms of features/bug-fixes should raise a pull request to the development branch.

3. **Release branch** During making a release to production environment, development branch will be checkout out to a new branch named release/version-name. The release/* branch can be created & pushed only by Maintainers. In the release branch, we will make a version commit & raise a pull request to the master branch.

4. **Create a Release Tag**- git tag -a vx.xx -m "change"; git push origin --tags
Live branch The master branch will contain the source code of the present/active release. Once a push is made to the master branch, we will release the code to the production environment.

5. **Feature development** For any new feature development, checkout the source from the default branch (development) & create your branch in form of the syntax: feature-feature-name

6. **Bug fixes / Enhancements** For any bug fixes or enhancements, checkout the source from the default branch (development) & create your branch in form of the syntax: enhancement-enhancement-name./bug-fix-bug-fix-name)


### Creating a new branch?
All Feature branches must be created under *feat* folder and all release branches should be created under *release* folder.

**Example:**
```
feat/demo-setup
release/2.5.0
```

### How to do a clean Commit?
Most of the cases commit should confine to a single task. Basically each granular task from your clickup tasklist can have a commit.(*Its okay to have multiple commits to a single task, but a single commit should not be covering multiple tasks*).

Each commit will have a short title and a description\
**Title Format:** #{clickup_task_id} Task Name\
**Description:** List Down all the changes done by you as a list.

**Example:**
```
#abc12 Demo Task Name
1. Change one
2. Change two
3. Change three
``` 

### How to raise a proper Merge Request?
Always raise your MRs **from feature branches to Development branch**.

Title should be short and relavant. Usually *name + one liner explanation* of the feature must suffice. Use the MR templete and add all the details mentioned in the MR templete. Make sure you add link to clickup task and assign reviewers. Please clear any merge conflicts before you assign the reviewer.

**Example:** Chat feature implementation
```
<!--Add link to Clickup list or task here-->
https://app.clickup.com/25541281/v/l/6-163266192-1

**Feature Description:**
<!--Add detailed description of the features implemented, also mention the changes you have made-->

1. Seller Channel creation on signup
2. Add Chat With Us option in the seller page
3. Chat Activity to load the chat page in the webview
4. Chat History Activity which can be accessed from the Profile section
5. Chat History Activity -> Has a recyclerview that displays the list of all the recent chats by a particular user(can be seller or buyer)

**Tech Doc and Miro Board:**
<!--Add links to respective tech docs and miro board. Make sure you turned on the link sharing-->
https://docs.google.com/document/d/1B7WnzxaqemBr6rmw4qOU47TEPR1Ucz9qJdp-rWAUY5g/edit?usp=sharing

**Demo Video:**
<!--If the feature implementation involves any visual changes or additions to the app. Make a loom video with the walkthrough of changes and add the link here-->
https://www.loom.com/share/cd4c067c9c8343108b62e47f86191b7f?sharedAppSource=personal_library

**Developers:**
<!--Tag @yourself and other developers who have worked on this feature-->
@nitish.gadangi1 @raghav.awasthi1 

**Reviewers:**
<!--Mention @name of the reviewers and also reviewer option on the MR to them-->
@nitish.gadangi1
```
