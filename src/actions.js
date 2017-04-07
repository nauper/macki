var toolComponent = null;
var placedItem = null;
var mousePos;
var boxArray = [];
var id = 0;
var loginbox = null;

//Select tool in toolbar
function selectTool(){
    moveTool.opacity = 0.5;
    textTool.opacity = 0.5;
    penTool.opacity = 0.5;
    toolItem.opacity = 1.0;
    window.selectedTool = toolItem.toolValue;
}
//Create textbox at mouse pos
function createTextBox(x, y){
    //if mouse is in toolbar
    mousePos = { x: x, y: y };
    if (mousePos.y < toolbox.height) {
        return;
    }
    toolComponent = Qt.createComponent("textbox.qml");
    if (toolComponent.status == Component.Loading)
        toolComponent.statusChanged.connect(createItem);
    else
        finishCreation();

    if ("undefined" != typeof boxArray) {
        boxArray.push(placedItem);
    } else {
        boxArray = [];
        boxArray.push(placedItem);
    }
    return placedItem;

}

function openLogin(){
    mousePos = { x: 100, y: 100};
    toolComponent = Qt.createComponent("loginBox.qml");
    if (toolComponent.status == Component.Loading)
        toolComponent.statusChanged.connect(createItem);
    else
        finishCreation();

    loginbox = placedItem;
    return;

}

// Ping at mouse position
function ping(x, y) {
    mousePos = { x: x, y: y };
    if (mousePos.y < toolbox.height) {
        return;
    }
    toolComponent = Qt.createComponent("ping.qml");
    if (toolComponent.status == Component.Loading) {
        toolComponent.statusChanged.connect(finishCreation);
    } else {
        finishCreation();
    }
}

// Finish creation of QML object
function finishCreation() {
    if (toolComponent.status == Component.Ready) {
        placedItem = toolComponent.createObject(window, {"userColor": window.userColor, "x": mousePos.x, "y": mousePos.y});
        if (placedItem == null) {
            console.log("Error creating object");
        }
    } else if (toolComponent.status == Component.Error) {
        console.log("Error loading component:", toolComponent.errorString());
    }
}






