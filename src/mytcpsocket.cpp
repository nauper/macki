#include "mytcpsocket.h"
#include <QDebug>
#include <vector>
#include <string>
#include <sstream>
#include <iostream>

using namespace std;

MyTcpSocket::MyTcpSocket(QObject *parent) : QObject(parent) {

}

// Connect to host through socket
void MyTcpSocket::doConnect() {
    socket = new QTcpSocket(this);

    connect(socket, SIGNAL(connected()), this, SLOT(connected()));
    connect(socket, SIGNAL(disconnected()),this, SLOT(disconnected()));
    connect(socket, SIGNAL(bytesWritten(qint64)),this, SLOT(bytesWritten(qint64)));
    connect(socket, SIGNAL(readyRead()),this, SLOT(readyRead()));

    qDebug() << "connecting...";

    // this is not blocking call
    //socket->connectToHost("146.185.169.60", 8990);
    socket->connectToHost("localhost", 8990);

    // we need to wait...
    if(!socket->waitForConnected(5000)) {
        qDebug() << "Error: " << socket->errorString();
    }
}



void MyTcpSocket::connected() {
    qDebug() << "connected...";
}

void MyTcpSocket::disconnected() {
    qDebug() << "disconnected...";
}

void MyTcpSocket::bytesWritten(qint64 bytes) {
    qDebug() << bytes << " bytes written...";
}

// Read data from socket
void MyTcpSocket::readyRead() {
    qDebug() << "reading...";

    // read the data from the socket
    QByteArray ba = socket->readAll();
    string instructionLine = ba.data();
    qDebug() << ba;
    istringstream instMul(instructionLine);
    string s;
    while(getline(instMul, s, '>')){
        string action = s.substr(1,4);
        vector<string> strings;
        string info;
        string st;
        // TODO: check message contents and emit appropriate signal
        if (action == "tbox") {
            info = s.substr(6, s.length()-6);
            istringstream f(info);
            while (getline(f, st, '-')) {
                qDebug() << QByteArray::fromStdString(st);
                strings.push_back(st);
            }
            emit doAddTextBox(stoi(strings[0]), stoi(strings[1]), stoi(strings[2]), QString::fromStdString(strings[3]), QString::fromStdString(strings[4]));

        } else if (action == "ping") {
            info = s.substr(6, s.length()-6);
            istringstream f(info);
            while (getline(f, st, '-')) {
                qDebug() << QByteArray::fromStdString(st);
                strings.push_back(st);
            }
            emit doPing(stoi(strings[0]), stoi(strings[1]));
        } else if (action == "mpos") {
            info = s.substr(6, s.length()-6);
            istringstream f(info);
            while (getline(f, st, '-')) {
                qDebug() << QByteArray::fromStdString(st);
                strings.push_back(st);
            }
        } else if (action == "writ") {
            info = s.substr(6, s.length()-6);
            istringstream f(info);
            while (getline(f, st, '-')) {
                qDebug() << QByteArray::fromStdString(st);
                strings.push_back(st);
            }
            emit doWrite(stoi(strings[0]), QString::fromStdString(strings[1]));
        } else if (action == "move") {
            info = s.substr(6, s.length()-6);
            istringstream f(info);
            while (getline(f, st, '-')) {
                qDebug() << QByteArray::fromStdString(st);
                strings.push_back(st);
            }
            emit doMove(stoi(strings[0]), stoi(strings[1]), stoi(strings[2]));
        } else if (action == "remv") {
            info = s.substr(6, s.length()-6);
            istringstream f(info);
            while (getline(f, st, '-')) {
                qDebug() << QByteArray::fromStdString(st);
                strings.push_back(st);
            }
            emit doRemove(stoi(strings[0]));
        } else if (action == "logi") {
            emit doLogin();
        } else if (action == "lgdn") {
            emit doLoginDone();
        }

        instMul.ignore(4, '\n');
    }


}

// Write a message to socket
void MyTcpSocket::cppSlot(const QString &msg) {
    QByteArray ba = msg.toLatin1();
    string s = ba.data();
    //const char* c_msg = s.c_str();
    socket->write((s + "\n").c_str());
    qDebug() << "wrote " << s.c_str();
}

