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

Copyright (C) 2002 Tomas Styblo <tripie@cpan.org>
*/

var g2submit_prefs = Components.classes["@mozilla.org/preferences-service;1"].
    getService(Components.interfaces.nsIPrefService).getBranch("g2submit.");

function g2submitReadPref(name) {
    if (g2submit_prefs.prefHasUserValue(name)) {
        var type = g2submit_prefs.getPrefType(name);
        if (type & 128) {
            return g2submit_prefs.getBoolPref(name);
        }
        else if (type & 64) {
            return g2submit_prefs.getIntPref(name);
        }
        else if (type & 32) {
            return g2submit_prefs.getCharPref(name);
        }
        else {
            return null;
        }
    }
    else {
        return null;
    }
}

function initG2submitPref() {
    document.getElementById("prefG2submitInterceptED2K").checked =
        g2submitReadPref("intercept.ed2k");
    document.getElementById("prefG2submitInterceptTorrent").checked =
        g2submitReadPref("intercept.torrent");
    document.getElementById("prefG2submitInterceptSig2dat").checked =
        g2submitReadPref("intercept.sig2dat");
	document.getElementById("prefG2submitInterceptMagnet").checked =
        g2submitReadPref("intercept.magnet");

    document.getElementById("prefG2submitED2KClient").value =
        g2submitReadPref("command.ed2k");
    document.getElementById("prefG2submitTorrentClient").value =
        g2submitReadPref("command.torrent");
    document.getElementById("prefG2submitSig2datClient").value =
        g2submitReadPref("command.sig2dat");
    document.getElementById("prefG2submitMagnetClient").value =
        g2submitReadPref("command.magnet");
}

function initG2submitPrefPanel() {
    parent.initPanel('chrome://g2submit/content/g2submit-pref.xul');
    initG2submitPref();
}

function changeG2submitPrefs() {
    g2submit_prefs.setBoolPref("intercept.ed2k",
        document.getElementById("prefG2submitInterceptED2K").checked);
    g2submit_prefs.setBoolPref("intercept.torrent",
        document.getElementById("prefG2submitInterceptTorrent").checked);
    g2submit_prefs.setBoolPref("intercept.sig2dat",
        document.getElementById("prefG2submitInterceptSig2dat").checked);
    g2submit_prefs.setBoolPref("intercept.magnet",
        document.getElementById("prefG2submitInterceptMagnet").checked);

    g2submit_prefs.setCharPref("command.ed2k",
        document.getElementById("prefG2submitED2KClient").value);
    g2submit_prefs.setCharPref("command.torrent",
        document.getElementById("prefG2submitTorrentClient").value);
    g2submit_prefs.setCharPref("command.sig2dat",
        document.getElementById("prefG2submitSig2datClient").value);
    g2submit_prefs.setCharPref("command.magnet",
        document.getElementById("prefG2submitSig2datClient").value);
}
