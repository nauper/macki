#ifndef MYCLASS_H
#define MYCLASS_H

#include <QObject>


class MyClass : public QObject {
    Q_OBJECT
public slots:
    void cppSlot(const QString &msg);
};

#endif // MYCLASS_H
