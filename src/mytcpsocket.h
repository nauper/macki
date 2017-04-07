#ifndef MYTCPSOCKET_H
#define MYTCPSOCKET_H

#include <QObject>
#include <QTcpSocket>
#include <QAbstractSocket>

class MyTcpSocket : public QObject
{
    Q_OBJECT
public:
    explicit MyTcpSocket(QObject *parent = 0);
    void doConnect();

signals:
    // TODO: Add signals for remaining actions
    void doAddTextBox(int index, int x, int y, QString color, QString text);
    void doPing(int x, int y);
    void doWrite(int index, QString text);
    void doMove(int index, int x, int y);
    void doRemove(int index);
    void doLogin();
    void doLoginDone();

public slots:
    void connected();
    void disconnected();
    void bytesWritten(qint64 bytes);
    void readyRead();
    void cppSlot(const QString &msg);

private:
    QTcpSocket *socket;
};

#endif // MYTCPSOCKET_H
