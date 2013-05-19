snaps
=====

A simple ref test runner for the rest of us.


What's all this about?
======================

In order to verify support of CSS (and maybe some small bit of other things) it relies on "reftests".  


What the heck is a "reftest"?
==============================

Basically, a ref test is something that loads two versions of a Web page - one that uses well-established means to draw the page, and one that uses some new feature (the one you are testing).  It then compares the two visually and if they match, the test passes.


So why write this?
==================

In a nutshell, unless you work for a vendor - it would appear that there is no way for you to run reftests yourself - for you (and apparently W3C too, it is a manual process of visual comparison).  That sucks because we want more people contributing tests for efforts like Test the Web Forward.  If you want to write a series of tests, there's no way for you to just periodically run them and see how browsers are doing.
That's one use case.  Closer to my heart though is something else: Prollyfills [The W3C Extensible Web Community Group](http://prollyfill.org) has a number of efforts to allow developers to extend the Web and propose new standards and/or work
along side working groups and browser vendors to implement evaluatable or experimental features *outside the browser itself*.  For CSS, this means we have no way to show tests and that sucks 
because as something matures, we can *totally* contribute reftests that working groups and vendors can use for the native implementations *before they even start implementing* and *while we are still standardizing*.  This would ensure that 
use cases are clearly and easily communicated which helps inform and better the specs themselves.


Ok, I get it - so this runs W3C reftests?
=========================================

Yes and no... W3C reftests are a little more complicated in that they contain lots of metadata describing how they are related and reported.  We're going for simple here on the 
first pass (I wrote this this morning in my hotel room) so this uses a separate file for metadata.  Still, it does the same basic thing and 
it can easily run/verify the same tests (metadata is non-visual) if you extract the metadata.  More importantly, this sort of thing should 
make it easy enough for a WG to evaluate and pick it up even if we take it no further - and it allows me to use them for CSS related prollyfills.


How do I use it?
=================
* Download the jar from the /dist directory
* edit the reftests.json or create your own
* invoke it from the command line passing the whole path to that file

```
$ java -jar Snaps-1.0-SNAPSHOT.jar /full/path/to/reftests.json
```

That's it... it will output some stuff to the command line and let you know what's up.  For example, the sample one provided will output:

```
-----------------------------------
Running tests for: firefox
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links.html
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links-expected.html
1 of 1 passed.
May 19, 2013 6:10:07 PM org.openqa.selenium.safari.SafariDriverServer start
INFO: Server started on port 43212
May 19, 2013 6:10:08 PM org.openqa.selenium.safari.SafariDriverChannelHandler$1 operationComplete
INFO: Connection opened
-----------------------------------
Running tests for: safari
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links.html
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links-expected.html
1 of 1 passed.
-----------------------------------
Running tests for: opera
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links.html
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links-expected.html
1 of 1 passed.
Started ChromeDriver
port=45546
version=0.8
log=/Users/bkardell/projects/java/snaps/target/chromedriver.log
-----------------------------------
Running tests for: chrome
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links.html
...fetching http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links-expected.html
1 of 1 passed.
```

JSON Structure
==============
The json has two properties, an array of browser names to test against and an array of tests.
Each test is an object, the only required thing is the path to the test itself (file protocol is just fine).
If you do not provide the "expected" property (as in the sample in this repo) it will look for "-expected.html" in the same place.

```
{
  "browsers": ["firefox", "safari", "opera" ],
  "tests": [
    {
      "test": "http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links.html",
      "expected": "http://bkardell.github.io/selectors-L4-link-prollyfills/examples/local/local-links-expected.html"
    }
  ]
}
```


A note about browsers
=========================
If you want to run chrome (or msie) you'll have a bit more to do.  For chrome, you'll have to [download the appropriate driver](https://code.google.com/p/chromedriver/downloads/list) and then set 
a property:

```
java -Dwebdriver.chrome.driver=/full/path/to/chromedriver2_mac32_0.8/chromedriver -jar Snaps-1.0-SNAPSHOT.jar  /full/path/to/reftests.json
```

Unfortunately, I don't have a PC handy, so the code support is there for MSIE, but I dont have testable instructions on how to set up the driver.  If you
want to send me a pull - much appreciated.



