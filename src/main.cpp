#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QDebug>
#include <QObject>
#include <QQmlContext>
#include "myclass.h"
#include "mytcpsocket.h"

int main(int argc, char *argv[]) {
    MyTcpSocket s;
    QCoreApplication::setAttribute(Qt::AA_EnableHighDpiScaling);
    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    engine.rootContext()->setContextProperty("mytcpsocket", &s);
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));

    QObject *rootObject = engine.rootObjects().first();
    s.doConnect();
    QObject::connect(rootObject, SIGNAL(qmlSignal(QString)),
                     &s, SLOT(cppSlot(QString)));

    return app.exec();
}
