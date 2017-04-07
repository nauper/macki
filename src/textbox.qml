import QtQuick 2.0
import QtQuick.Controls 2.1

Rectangle {
    property int index
    id: textboxes
    property color userColor
    color: userColor
    width: 150; height: 150;
    border.color: "black"
    property bool writeBack: true
    function getText() {
        return boxText.text
    }

    function setText(newText) {
        if (boxText.text != newText) {
            boxText.text = newText
        }
    }

    function moveBox(newX, newY){
        /* Animation för att flytta textboxen om den endast uppdateras när den släpps
        xmove.to = newX
        ymove.to = newY
        moveAni.start()
        */

        textboxes.x = newX
        textboxes.y = newY

    }

    function remove(){
        textboxes.destroy();
    }
    ParallelAnimation{
        id: moveAni
        PropertyAnimation{id: xmove; target: textboxes; property: "x"; duration: 500}
        PropertyAnimation{id: ymove; target: textboxes; property: "y"; duration: 500}
    }

    //Text area
    TextArea {
        id: boxText
        width: parent.width - 10
        height: parent.height - 10
        anchors.centerIn: parent
        wrapMode: TextArea.Wrap
        property int maximumLines: 7
        property string previousText: text
        onTextChanged: {
            if (lineCount > maximumLines) {
                var cursor = cursorPosition;
                textboxes.setText(previousText)
                if (cursor > text.length) {
                    cursorPosition = text.length;
                } else {
                    cursorPosition = cursor-1;
                }
            } else {
                if(writeBack){
                    item.qmlSignal("<writ-"+index+"-"+text+">")
                }


            }
            previousText = text

        }
    }

    //Move bar
    Rectangle {
        width: parent.width - 10
        height: 10
        anchors.left: parent.left
        anchors.top: parent.top
        color : "darkgray"
        MouseArea{
            anchors.fill: parent
            drag.target: textboxes

            /* Endast uppdaterad när textboxen är släppt
            onReleased: {
                item.qmlSignal("<move-"+index+"-"+textboxes.x+"-"+textboxes.y+">")
            }
            */
            //Uppdaterar varje gång den rör sig, laggar mer.
            onPositionChanged: {
                item.qmlSignal("<move-"+index+"-"+textboxes.x+"-"+textboxes.y+">")
            }
        }
    }

    //Close button
    Image {
        anchors.top: parent.top
        anchors.right: parent.right
        source: "images/cross.png"
        MouseArea {
            anchors.fill: parent
            onPressed: item.qmlSignal("<remv-"+index+">")
        }
    }

}
