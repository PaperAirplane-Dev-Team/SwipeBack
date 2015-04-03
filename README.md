SwipeBack
---
[![Build Status](https://travis-ci.org/PaperAirplane-Dev-Team/SwipeBack.svg?branch=master)](https://travis-ci.org/PaperAirplane-Dev-Team/SwipeBack)

![BANNER](https://raw.githubusercontent.com/PaperAirplane-Dev-Team/SwipeBack/master/art/banner.png)

![LOGO](https://raw.githubusercontent.com/PaperAirplane-Dev-Team/SwipeBack/master/art/Logo_FullSize_512.png)

SwipeBack is an Xposed module which implements a global swipe-to-return gesture inspired by [SwipeBackLayout](https://github.com/Ikew0ng/SwipeBackLayout) but more than swipe-to-return.

How to build
---
If you build with gradle - pull branch `master`  
If you build with AIDE - pull branch `aide`

Contributing
---
1. Fork
2. Create a new branch `patch-X`out of branch `master`
3. Modify
4. Send a pull request to branch `master` here. Do not send pull request to `aide`.
5. NOTICE: DO NOT make a commit named "Routine:" or "Merge in xxx after reset to xxx"
6. NOTICE: DO NOT INCLUDE ANY CHANGE TO BUILD CONFIGS IN REGULAR COMMITS

Why anothor branch 'aide'?
---
The branch 'aide' is for builds with the AIDE(Android IDE), which lacks support of adding a jar as a reference. So we have to do some hacks that is not compatible with normal gradle. The two branches are synced automatically by a script.

License
---
See [LICENSE](https://raw.githubusercontent.com/PaperAirplane-Dev-Team/SwipeBack/master/LICENSE)
