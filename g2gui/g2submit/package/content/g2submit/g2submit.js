/*
The contents of this file are subject to the Mozilla Public
License Version 1.1 (the "License"); you may not use this file
except in compliance with the License. You may obtain a copy of
the License at http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS
IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
implied. See the License for the specific language governing
rights and limitations under the License.

Alternatively, the contents of this file may be used under the
terms of the GNU General Public License Version 2 or later (the
"GPL"), in which case the provisions of the GPL are applicable
instead of those above.

Copyright (C) 2003 Tomas Styblo <tripie@cpan.org>
*/


var g2submit_default_tmpdir = "/tmp";  // unix
var g2submit_dir_separator = '/';      // unix
var g2submit_os = 'unix';              // unix
if (window.navigator.platform.toLowerCase().indexOf("win") != -1) {
    g2submit_default_tmpdir = "C:\\windows\\temp";     // windows
    g2submit_dir_separator = '\\';                     // windows
    g2submit_os = 'win';                               // windows
}
var g2submit_alert_error = "G2 Submit Error: ";
var g2submit_link_node = null;


function g2submitInit(evt) {
    window.removeEventListener("load", g2submitInit, true);
    getBrowser().addEventListener("mousedown", g2submitHandleMouseDown, true);
    getBrowser().addEventListener("mouseup", g2submitHandleMouseIntercept, true);
    getBrowser().addEventListener("click", g2submitHandleMouseIntercept, true);
}


function g2submitGetPrefCommand(name) {
    var cmd = g2submitReadPref("command" + '.' + name);
    if (cmd == null || cmd.length == 0) {
        g2submitError("no '" + name + "' command is set");
        return null;
    }
    else {
        return cmd;
    }
}


function g2submitHandleMouseDown(evt) {
    var elem_type = null;
    var link_type = null;

    g2submit_document = evt.originalTarget.ownerDocument;
	for(var node = evt.originalTarget; node != null; node = node.parentNode) {
		if(node.nodeType == Node.ELEMENT_NODE) {
            elem_type = node.localName.toUpperCase();
            if (elem_type == "A" || elem_type == "AREA") {
                //link_type = g2submitGetProtocol();
                g2submit_link_node = node;
                link_type = g2submitGetProtocol(node);
				//g2submitError("LINK TYPE: " + link_type);
		// we break to see which mouse-button was pressed
		break;
            }
        }
    }

    if (evt.button == 0 && link_type != null) {
        // left mouse button - intercept now
        if (link_type == "ed2k" && g2submitReadPref("intercept.ed2k")) {
	    evt.preventDefault(); evt.stopPropagation();
            g2submitED2KClient();
            return false;
        }
        else if (link_type == "torrent" && g2submitReadPref("intercept.torrent")) {
            evt.preventDefault(); evt.stopPropagation();
            g2submitTorrentClient();
            return false;
        }
        else if (link_type == "sig2dat" && g2submitReadPref("intercept.sig2dat")) {
            evt.preventDefault(); evt.stopPropagation();
            g2submitSig2datClient();
            return false;
        }
        else if (link_type == "magnet" && g2submitReadPref("intercept.magnet")) {
            evt.preventDefault(); evt.stopPropagation();
            g2submitMagnetClient();
            return false;
        }

	}
    else if (evt.button == 2) {
        // right mouse button - popup menu
        document.getElementById("g2submit-link-ed2k").setAttribute('disabled',
            !(link_type == "ed2k"));
		document.getElementById("g2submit-link-torrent").setAttribute('disabled',
            !(link_type == "torrent"));
		document.getElementById("g2submit-link-sig2dat").setAttribute('disabled',
            !(link_type == "sig2dat"));
		document.getElementById("g2submit-link-magnet").setAttribute('disabled',
            !(link_type == "magnet"));
		document.getElementById("g2submit-link-http").setAttribute('disabled',
            !(link_type == "http"));
        

	}
}

function g2submitHandleMouseIntercept(evt) {
    var elem_type = null;
    var link_type = null;
	for(var node = evt.originalTarget; node != null; node = node.parentNode) {
		if(node.nodeType == Node.ELEMENT_NODE) {
            elem_type = node.localName.toUpperCase();
            if (elem_type == "A" || elem_type == "AREA") {
                link_type = g2submitGetProtocol(node);
                break;
            }
        }
    }

    if (evt.button == 0 && link_type != null) {
        if ( (link_type == "ed2k" && g2submitReadPref("intercept.ed2k")) ||
            ((link_type == "torrent") && g2submitReadPref("intercept.torrent")) ||
			((link_type == "sig2dat") && g2submitReadPref("intercept.sig2dat")) ||
			((link_type == "magnet") && g2submitReadPref("intercept.magnet"))
			) {
            evt.preventDefault(); evt.stopPropagation();
            return false;
        }
	}
}


//function g2submitGetProtocol(node) {

function g2submitGetProtocol(node) {
    var href = node.getAttribute("href");

	var protocol = href.substring(0, href.indexOf(':')).toLowerCase();
    var extension = href.substring(href.lastIndexOf('.'),href.length).toLowerCase();
    // debug helper
	//g2submitError("EXTENSION: |" + extension + "| PROTOCOL: " + protocol + " HREF: " + href);

	// check for a .torrent extension
	if (extension == ".torrent") {
        return "torrent";
	} else {
        return protocol;
	}

}

function g2submit_unescape(str) {
    var buf = str.replace(/\+/g, " ");
    buf = decodeURIComponent(buf);
    return buf;
}


function g2submitED2KClient() {
    if (g2submit_link_node) {
        var href = g2submit_link_node.getAttribute("href");
        var esc = {
            'l': href
        };
        g2submitRunProgram("ed2k client", g2submitGetPrefCommand("ed2k"), esc);
    }
    else {
        g2submitError("no link node found");
    }
}

function g2submitTorrentClient() {
	if (g2submit_link_node) {
        var href = g2submit_link_node.getAttribute("href");
        if (href.length == 0) {
            g2submitError("url is empty");
        }
        else {
            if (href.indexOf("://") == -1) {
                // convert to absolute URL
                var base = g2submit_document.URL;
                if (base.charAt(base.length - 1) == '/') {
                    href = base + href;
                }
                else {
                    if (href.charAt(0) == '/') {
                        href = base.substring(0,
                            base.indexOf('/', base.indexOf("//") + 2)) + href;
                    }
                    else {
                        href = base.substring(0, base.lastIndexOf('/')) + '/' + href;
                    }
                }
            }
	    }
        var esc = {
            'l': href
        };
        g2submitRunProgram("torrent client", g2submitGetPrefCommand("torrent"), esc);
	} else {
        g2submitError("no link node found");
	}
}

function g2submitHttpClient() {
	if (g2submit_link_node) {
        var href = g2submit_link_node.getAttribute("href");
        if (href.length == 0) {
            g2submitError("url is empty");
        }
        else {
            if (href.indexOf("://") == -1) {
                // convert to absolute URL
                var base = g2submit_document.URL;
                if (base.charAt(base.length - 1) == '/') {
                    href = base + href;
                }
                else {
                    if (href.charAt(0) == '/') {
                        href = base.substring(0,
                            base.indexOf('/', base.indexOf("//") + 2)) + href;
                    }
                    else {
                        href = base.substring(0, base.lastIndexOf('/')) + '/' + href;
                    }
                }
            }
	    }
        var esc = {
            'l': href
        };
        g2submitRunProgram("http client", g2submitGetPrefCommand("http"), esc);
	} else {
        g2submitError("no link node found");
	}
}

function g2submitSig2datClient() {
    if (g2submit_link_node) {
        var href = g2submit_link_node.getAttribute("href");
        var esc = {
            'l': href
        };
        g2submitRunProgram("sig2dat client", g2submitGetPrefCommand("sig2dat"), esc);
    }
    else {
        g2submitError("no link node found");
    }
}

function g2submitMagnetClient() {
    if (g2submit_link_node) {
        var href = g2submit_link_node.getAttribute("href");
        var esc = {
            'l': href
        };
        g2submitRunProgram("magnet client", g2submitGetPrefCommand("magnet"), esc);
    }
    else {
        g2submitError("no link node found");
    }
}



function g2submitRunProgram(context, cmd, esc) {
    if (cmd == null) {
        return false; // no command is set
    }

	var replaced = false;

    var args = new Array();
    var scmd = cmd.split(/\ /);
    var executable = scmd.shift();

	if (executable.length == 0) {
        g2submitError(context + ": no executable in command");
        return false;
    }

    for (var i = 0; i < scmd.length; i++) {
        
        
        var param = scmd[i];
        var buf = "";
        if (param.length == 0) {
            continue;
        }
        for (var e = 0; e < param.length; e++) {
            var c = param[e];
            if (c == '%') {
                var a = param[++e];
                if (esc[a] === undefined) {
                    g2submitError(context + ": unknown escape in command '" + cmd + "': %" + a);
                    return false;
                }
                else {
                    buf += esc[a];
                    replaced = true;
                }
            }
            else {
                buf += c;
            }
        }
        //buf += scmd[i];
		args.push(buf);
    }
	
	// incase that no %l has been given, we add it automatically 
	if (replaced == false) {
		args.push(esc['l']);
	}

    try {
        var exec = Components.classes["@mozilla.org/file/local;1"].
                   createInstance(Components.interfaces.nsILocalFile);
        var pr = Components.classes["@mozilla.org/process/util;1"].
                 createInstance(Components.interfaces.nsIProcess);
        var path = null;

        // $PATH stuff (only on UNIX)
        if (g2submit_os == 'unix') {
            try {
                path = pr.getEnvironment("PATH").split(":");
            }
            catch (e) {
                // do nothing
            }
        }

        // If executable is an absolute path, run it or fail.  If not, then
        // look for it in $PATH.  FIXME: How do you tell portably if a path is
        // absolute?
        if (executable.charAt(0) == "/" || path == null) {
            exec.initWithPath(executable);
            if (! exec.exists()) {
                g2submitError(context + ": executable '" + executable + "' does not exist.");
                return false;
            }
        }
        else {
            var found = false;
            for (i = 0; i < path.length; i++) {
                exec.initWithPath(path[i]);
                exec.appendRelativePath(executable);
                if (exec.exists()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                g2submitError(context + ": could not find '" + executable + "' in path.");
                return false;
            }
        }
        pr.init(exec);
        pr.run(false /* don't block */, args, args.length);
    }
    catch (e) {
        g2submitError(context + ": cannot run executable '" +
                   executable + "' (args: " + args + "): " + e);
        return false;
    }
    //alert(pr.pid); /* pid is not implemented :((( */
    return true;
}

// A little function to pop up a warning window
function g2submitError(msg) {
    alert(g2submit_alert_error + msg);
}

/* Run run run */
window.addEventListener("load", g2submitInit, true);

