from socket import *
import _thread
import random

# -*- coding: UTF-8 -*-

class Node:
    def __init__(self, Name, Socket=None, next=None):
        self.Name = Name
        self.Socket = Socket
        self.next = next

def init_list():
    global node1
    node1 = Node('no player')

def delete_node(Name):
    global node1
    pre_node = node1
    next_node = pre_node.next

    if pre_node.Name == Name:
        node1 = next_node
        del pre_node
        return
    
    while next_node:
        if next_node.Name == Name:
            pre_node.next = next_node.next
            del next_node
            break
        pre_node = next_node
        next_node = next_node.next
 
def insert_node(Name, Socket):
    global node1
    new_node = Node(Name, Socket)
    new_node.next = node1
    node1 = new_node

# linked list

cltnumber = 0  # cltnumber: 게임을 하고 있는 플레이어들의 수
teacherID = ''  # teacherID: '선생님'이라는 권한을 가지고 있는 플레이어의 아이디
haveTeacher = 0  # 선생님의 존재 여부 (0: 선생님이 없음, 1: 선생님이 있음)
answer = ''  # answer: 선생님이 출제한 문제의 답

def isDuplicate(username):  # 플레이어의 아이디가 중복되는지 체크함
     global cltnumber
     global node1

     node = node1
     while node:
          if node.Name == username:
               return True

          node = node.next

     return False

def toChosong(string):  # 한글로 된 낱말의 자음만 추출해서 return함
     Cho = ['ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
            'ㅇ' , 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ']
     result = ""

     for i in range(0,len(string)):
          han = string[i]

          gelja = ord(han) - 44032
          c1 = gelja / 588
          c1 = int(c1)

          result = result + Cho[c1]

     return result

def sendImage(msg):  # 이미지를 학생들한테 전송
     global teacherID
     global cltnumber
     global node1

     node = node1
     while node:
        if node.Name != teacherID and node.Name != 'no player':
            connectionSocket = node.Socket
            connectionSocket.send(msg)
            
        node = node.next

def sendToPlayer(playerID, msg):  # 해당 플레이어한테 명령문을 전송함
     global cltnumber
     global node1

     msg = msg + ' end'

     msg = msg.encode('UTF-8')

     node = node1
     while node:
        if node.Name == playerID:
            connectionSocket = node.Socket  # 명령문을 encoding 한 후 clients에 저장된 소켓을 꺼냄으로써 명령문을 클라이언트한테 전송함
            connectionSocket.send(msg)
            break
          
        node = node.next  

def sendToStudents(msg):  # 학생들한테 명령문을 전송함
     global teacherID
     global cltnumber
     global node1

     msg = msg + ' end'

     msg = msg.encode('UTF-8')

     node = node1
     while node:
        if node.Name != teacherID and node.Name != 'no player':
            connectionSocket = node.Socket
            connectionSocket.send(msg)
            
        node = node.next

def sendToAll(msg):  # 모든 플레이어한테 명령문을 전송함
     global cltnumber
     global node1

     msg = msg + ' end'

     msg = msg.encode('UTF-8')

     node = node1
     while node:
        if node.Name != 'no player':
            connectionSocket = node.Socket
            connectionSocket.send(msg)
            
        node = node.next

def newTeacher():  # 선생님이 게임에서 나갔을 경우 랜덤으로 선생님을 선발하는 함수
     global cltnumber
     global teacherID
     global node1
     global haveTeacher

     if cltnumber != 0:
          if cltnumber == 1:  # 한 사람만 남았을 경우 랜덤 선발은 의미가 없음
               rand = 1
          else:
               rand = random.randint(1, cltnumber-1)
               
          i = 0;
          node = node1
          while i < rand:
               node = node.next
               i = i+1

          teacherID = node.Name
                    
          sendToPlayer(teacherID, 'setTeacher')
          sendToAll('notQuized ' + teacherID)  # 선생님을 새로 지정하는 명령문을 선발된 클라이언트한테 보내고 문제 출제 여부를 false로 바꿔줌
     else:
         haveTeacher = 0

def execute(connectionSocket):  # server thread

     global cltnumber
     global teacherID
     global answer
     global haveTeacher
     global node1

     orderinfo = ['','','','','','','','','','']  # orderinfo[]: 명령문을 띄어쓰기를 기준으로 분리해서 저장하기 위한 배열

     try:
          msg = connectionSocket.recv(1024)
          msg = msg.decode('UTF-8')  # 클라이언트로부터 플레이어의 아이디와 타입(학생/선생님)을 받음
     
          usrinfo = msg.split()  # 플레이어의 정보를 띄어쓰기를 기준으로 분리한 후 배열 속에 저장함
                                 # usrinfo[0]: 플레이어의 아이디
                                 # usrinfo[1]: 플레이어의 타입 (Student or Teacher)

          if cltnumber >= 8 or isDuplicate(usrinfo[0]) is True or (usrinfo[1] == 'Teacher' and haveTeacher == 1):  # 인원 수가 초과했거나 아이디가 중복되거나 선생님이 이미 서버에 접속해 있을 경우
               string = 'setUnable end'
               string = string.encode('UTF-8')
               connectionSocket.send(string)
               
               connectionSocket.close()
               return -1  # 무효화 명령문을 보낸 후 접속을 끊어버림

          print(usrinfo[0], '님이 서버에 접속하셨습니다.')

          insert_node(usrinfo[0], connectionSocket)
          cltnumber = cltnumber+1

          if usrinfo[1]=='Teacher':  # 플레이어가 선생님일 경우 선생님의 아이디를 저장하고 선생님의 존재 여부를 true로 설정함
               teacherID = usrinfo[0]
               haveTeacher = 1

          print('현재 서버 접속자 수는 ', cltnumber, '입니다.')
          print('')

          while msg is not None:
               msg = connectionSocket.recv(1024)
               msg = msg.decode('UTF-8')  # 클라이언트로부터 명령문을 받은 후 decoding 함

               if msg is None:
                    continue

               orderinfo = msg.split()  # 명령문을 띄어쓰기를 기준으로 분리한 후 배열 속에 저장함

               if orderinfo[0] == 'setQuiz':  # 선생님이 문제를 출제했을 경우
                    sendToAll('allClear')
                    sendToStudents('setQuiz ' + toChosong(orderinfo[1]))  # 학생이 setQuiz 명령을 받으면 화면이 clear된 다음 문제가 보여짐
                    sendToPlayer(teacherID, 'setQuiz ' + orderinfo[1])  # 선생님이 setQuiz 명령을 받으면 화면이 clear된 다음 입력했던 정답을 다시 보여주고 힌트를 입력받음
                    answer = orderinfo[1]
                    
               else:
                    if orderinfo[0] == 'setHint':  # 선생님이 힌트를 입력했을 경우 학생들한테 힌트를 전송함
                         sendToStudents('setHint ' + msg[8:])
                    
                    else:
                         if orderinfo[0] == 'inputAnswer':
                              if answer == orderinfo[2]:  # 학생이 답안을 입력했는데 그 답안이 정답일 경우
                                   sendToAll('showAnswer ' + answer + ' ' + orderinfo[1])  # 정답을 공개하고 누가 정답을 맟췄는지 알려줌
                                   sendToPlayer(orderinfo[1], 'setTeacher')
                                   sendToPlayer(teacherID, 'unsetTeacher')  # '선생님'이라는 권한을 넘겨줌
                                   teacherID = orderinfo[1]  # 선생님을 새로 설정함
                         else:
                              if orderinfo[0] == 'submitImage':
                                   sendToStudents('submitImage ' + orderinfo[1])

                                   i = 0
                                   while i < int(orderinfo[1]):
                                        msg = connectionSocket.recv(1024)
                                        sendImage(msg)
                                        i = i + 1020
          
     except Exception as e:
          print('')

     # 플레이어가 게임에서 나갔을 경우

     delete_node(usrinfo[0])

     print(usrinfo[0], '님이 서버에서 나가셨습니다.')
     cltnumber = cltnumber-1
     print('현재 서버 접속자 수는 ', cltnumber, '입니다.')
     print('')  # 플레이어들의 수를 1만큼 감소시킴
          
     if usrinfo[0] == teacherID:  # 게임에서 나간 사람이 선생님일 경우 선생님을 새로 선발함
          newTeacher()

     connectionSocket.close()

     return 0

# main

try:
     portNumber = 8889  # 포트 번호 생성
     serverSocket = socket(AF_INET, SOCK_STREAM)
     serverSocket.bind(('', portNumber))
     serverSocket.listen(1)  # socket을 TCP Type으로 지정함
     print('서버가 정상적으로 시작되었습니다.')
     print('Port Number: ', portNumber)
     init_list()

     while True:
          connectionSocket, addr = serverSocket.accept()

          print('connected!')

          _thread.start_new_thread(execute, (connectionSocket,))  # 플레이어가 접속하면 thread를 실행시킴

except Exception as e:
     print('Error: ', e)
