function displayDiv(buttonId) {
    var dictionary = {'extensionLabel':"extension-panel", 'mimeLabel':"mime-panel", 'patternLabel':"pattern-panel"};
    for (var button in dictionary){
        if(button === buttonId){
            document.getElementById(dictionary[button]).removeAttribute("hidden");
            console.log("ok");
        } else {
            document.getElementById(dictionary[button]).setAttribute("hidden", "");
            console.log("ko");
        }
    }
}