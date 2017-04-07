import QtQuick 2.0
import QtQuick.Controls 2.1

Rectangle {
    id: loginBox
    color: "Gray"
    width: 150; height: 150;
    border.color: "Black"

    function remove(){
        loginBox.destroy();
    }

    Rectangle{
        color: "Black"
        width: userNameInput.width+5
        height: userNameInput.height+5
        anchors.horizontalCenter: parent.horizontalCenter
        TextInput{
            maximumLength: 7
            width: 65
            height: 30
            id: userNameInput
            text: "userNamn"

            color: "White"
            anchors.verticalCenter: parent.verticalCenter
            anchors.horizontalCenter: parent.horizontalCenter


        }
    }

    Rectangle{
        color: "Green"
        width: passwordInput.width+5
        height: passwordInput.height+5
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.top: userNameInput.parent.bottom
        TextInput{
            maximumLength: 7
            width: 65
            height: 30
            id: passwordInput
            text: "passw"

            color: "White"
            anchors.verticalCenter: parent.verticalCenter
            anchors.horizontalCenter: parent.horizontalCenter


        }
    }

    Button{

        width: passwordInput.width+5
        height: passwordInput.height+5
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.top: passwordInput.parent.bottom

        onPressed: item.qmlSignal(userNameInput.text+"-"+passwordInput.text)

    }




}
