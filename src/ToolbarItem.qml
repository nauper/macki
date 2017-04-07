import QtQuick 2.0
import "actions.js" as Action


Image {
    id: toolItem

    property string componentFile
    property int toolValue


    MouseArea {
        anchors.fill: parent
        onClicked: Action.selectTool();
}

}
