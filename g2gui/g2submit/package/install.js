// this function verifies disk space in kilobytes
function verifyDiskSpace(dirPath, spaceRequired)
{
  var spaceAvailable;

  // Get the available disk space on the given path
  spaceAvailable = fileGetDiskSpaceAvailable(dirPath);

  // Convert the available disk space into kilobytes
  spaceAvailable = parseInt(spaceAvailable / 1024);

  // do the verification
  if(spaceAvailable < spaceRequired)
  {
    logComment("Insufficient disk space: " + dirPath);
    logComment("  required : " + spaceRequired + " K");
    logComment("  available: " + spaceAvailable + " K");
    return(false);
  }

  return(true);
}

var srDest = 1;
var version = "0.2";
var err = initInstall("g2submit", "g2submit", version);
logComment("initInstall: " + err);

// profile installs only work since 2003-03-06
const INST_TO_PROFILE = "Do you wish to install g2submit to your user profile ?\nPress <Cancel> for application directory.";
var instToProfile = confirm(INST_TO_PROFILE);
var chromef = instToProfile ? getFolder("Profile", "chrome") : getFolder("chrome");
var jar = getFolder(chromef, "g2submit.jar");

if (verifyDiskSpace(getFolder("Program"), srDest))
{
    addFile("g2submit",
            "g2submit.jar", // jar source folder
            chromef,        // target folder
            "");                        // target subdir 

    if (instToProfile) {
        registerChrome(CONTENT | PROFILE_CHROME, jar, "content/g2submit/");
    }
    else {
        registerChrome(CONTENT | DELAYED_CHROME, jar, "content/g2submit/");
    }

    if (err == SUCCESS) {
        performInstall(); 
        //alert("Please restart Mozilla/Firebird to complete the installation."); 
    }
    else {
        cancelInstall(err);
    }
}
else
    cancelInstall(INSUFFICIENT_DISK_SPACE);
