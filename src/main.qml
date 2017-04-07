import QtQuick 2.7
import QtQuick.Controls 2.1
import QtQuick.Controls.Styles 1.4
import QtQuick.Layouts 1.0
import QtQuick.Window 2.0
import "actions.js" as Action

ApplicationWindow {
    id: item
    signal qmlSignal(string msg)
    visible: true
    title: qsTr("Title")

    //var tvungen att ändra då det blev konstigt när jag hdae flera skärmar
    height: 1000; width: 800
    //width: Screen.desktopAvailableWidth
    //height: Screen.desktopAvailableHeight

    Connections {
        // TODO: add handlers for remaining actions.
        target: mytcpsocket
        onDoPing: {
            Action.ping(x, y)
        }

        onDoWrite: {
            var box = Action.boxArray[index]
            box.writeBack = false
            box.setText(text)
            box.writeBack = true
        }

        onDoAddTextBox: {
            var box = Action.createTextBox(x, y)
            box.setText(text)
            box.index = index
            box.userColor = color
        }

        onDoMove: {
            var box = Action.boxArray[index]
            box.moveBox(x, y)
        }

        onDoRemove:{
            var box = Action.boxArray[index]
            box.remove();
            //TODO remove from boxArray, but will ruin index
        }

        onDoLogin: {
            area.enabled = false
            Action.openLogin()
        }

        onDoLoginDone: {
            Action.loginbox.remove()
            area.enabled = true
        }
    }

    Item{
        focus: true
        id: window
        anchors.fill: parent
        //Current selected tool
        property int selectedTool: 1
        property string userColor: "#00b200"
        property bool altKey: false

        //Flag whenever alt is pressed
        Keys.onPressed: {
            if (event.key == Qt.Key_Alt){
                altKey = true;
            }

        }

        //Change flag when alt is released
        Keys.onReleased: {
            if (event.key == Qt.Key_Alt){
                altKey = false;
            }
        }

        /*

          Click area
            Case:
           1. Move Tool and ping
           2. TextBox tool
           3. Drawing tool
        */

        MouseArea {
            id: area
            anchors.fill: parent
            onPressed: {
                switch (parent.selectedTool){
                case 1:
                    // If the alt key is held, ping on current mouse location
                    if(window.altKey){
                        item.qmlSignal("<ping-"+mouse.x+"-"+mouse.y+">")
                        console.log("<ping-"+mouse.x+"-"+mouse.y+">")
                    } else {
                    }

                    break;
                case 2:
                    // Create a textbox at current mouse location
                    console.log("<tbox-"+(Action.boxArray.length)+"-"+mouse.x+"-"+mouse.y+"-"+window.userColor+"-"+"text...>")
                    item.qmlSignal("<tbox-"+(Action.boxArray.length)+"-"+mouse.x+"-"+mouse.y+"-"+window.userColor+"-"+"text...>")
                    break;
                case 3:
                    // Draw a line
                    drawing.lastX = mouseX
                    drawing.lastY = mouseY
                    break;
                }
            }
            onPositionChanged: {
                if(parent.selectedTool == 3){
                    drawing.requestPaint()
                }
            }
        }

        //WORKSPACE
        Rectangle {
            id: workspace
            anchors { left: parent.left; top: toolbox.bottom; right: parent.right;  bottom: parent.bottom }
            Image {
                anchors.centerIn: parent
                source: "images/pdfbak.png"
            }
            gradient: Gradient {
                GradientStop { id: gradientStopA; position: 0.0; color: "#ffae35" }
                GradientStop { id: gradientStopB; position: 1.0; color: "#a56300" }
            }
        }

        //TOOLBOX
        Rectangle {
            id: toolbox
            height: 70
            color: "lightgray"
            anchors { right: parent.right; top: parent.top; left: parent.left}
            border.color: "black"

            //TOOLS
            Rectangle {
                anchors.centerIn: parent
                width: tools.width + 10; height: tools.height + 10
                border.color: "black"
                color: "darkgray"

                Row {
                    id: tools
                    anchors.centerIn: parent
                    spacing: 10

                    ToolbarItem {
                        id: moveTool
                        anchors.verticalCenter: parent.verticalCenter
                        width: 40
                        height: 40
                        opacity: 1.0
                        source: "images/cursor.png"
                        toolValue: 1
                    }
                    ToolbarItem {
                        id: textTool
                        anchors.verticalCenter: parent.verticalCenter
                        width: 40
                        height: 40
                        opacity: 0.5
                        componentFile: "textbox.qml"
                        source: "images/textbox.png"
                        toolValue: 2

                    }

                    ToolbarItem {
                        id: penTool
                        anchors.verticalCenter: parent.verticalCenter
                        width: 40
                        height: 40
                        opacity: 0.5
                        source: "images/pencil.png"
                        toolValue: 3

                    }
                    Slider{
                        id: penSize
                        orientation: "Vertical"
                        height: 40
                        width: 10
                        handle.width: 10
                        handle.height: 10
                        from: 1
                        to: 5
                        stepSize: 0.1

                    }

                }
            }


            //Color selector
            Rectangle{
                height: colors.height+10
                width: colors.width+10
                anchors { right: parent.right; verticalCenter: parent.verticalCenter; rightMargin: 20}
                color: "darkgray"

                Row{
                    id: colors
                    leftPadding: 10
                    spacing: 10
                    anchors.verticalCenter: parent.verticalCenter

                    //hex input for color
                    TextInput{
                        maximumLength: 7
                        width: 65
                        height: 30
                        id: colorInput
                        text: window.userColor
                        color: "white"
                        anchors.verticalCenter: parent.verticalCenter


                    }
                    //Square to display the current color and update when pressed
                    Rectangle{
                        id: colorSquare
                        width: 50
                        height: 50
                        color: window.userColor
                        //Update color square
                        Timer {
                            interval: 100; running: true; repeat: true
                            onTriggered: {
                                window.userColor= colorInput.text
                                colorSquare.color = window.userColor
                            }
                        }
                    }



                }
            }
        }
    }
    //http://qmlbook.github.io/ch07/index.html
    Canvas{
        id: drawing
        anchors.fill: parent

        property real lastX
        property real lastY

        onPaint: {
            var ctx = getContext('2d')
            ctx.lineWidth = penSize.value
            ctx.strokeStyle = window.userColor
            ctx.beginPath()
            ctx.moveTo(lastX, lastY)
            lastX = area.mouseX
            lastY = area.mouseY
            ctx.lineTo(lastX, lastY)
            ctx.stroke()
        }
    }
}
