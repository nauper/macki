import QtQuick 2.7
Rectangle{
    property int circleSize: 40
    property int borderSize: 5
    property color userColor
    property int animationTime: 500
    color: "transparent"

    Rectangle {        
        id: ping
        width: parent.circleSize; height: parent.circleSize
        border.color: parent.userColor
        color: "transparent"
        radius: width/2
        border.width: parent.borderSize
        y: -width/2
        x: -height/2

        ScaleAnimator {
            target: ping;
            from: 1;
            to: 0;
            duration: animationTime
            running: true
        }

        Timer {
            interval: animationTime; running: true; repeat: false
            onTriggered: ping.destroy();
        }

    }
}
