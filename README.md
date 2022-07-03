## ChattingRoom

基于socket实现的简易聊天室

### 功能介绍

- 在线用户一对一私聊
- 用户群聊
- 用户与服务器对话
- 服务器广播
- 文件传输

### 使用说明

#### 进入聊天室

​		首先运行Server程序创建服务端。

​		运行``Client``，根据输出提示创建昵称，随后即可开始聊天。

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/0_init.jpg" alt="image-20220702234513280" style="zoom:50%;" />

​	各客户端之间相互独立，线程安全，支持多个客户端并发执行。服务器端可实时显示在线人数：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/1_serverReflect.png" alt="image-20220703022625702" style="zoom:50%;" />

#### 在线用户一对一私聊

​	在某一客户端中，按照``@Specific  聊天对象昵称  信息内容``的格式输入命令，可向目标对象发送消息：

​	如图，用户Vera向用户Tom发送信息：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/2.png" alt="image-20220702234802936" style="zoom:50%;" />

Tom将收到如下信息提示：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/3.png" alt="image-20220702234842341" style="zoom:50%;" />

#### 在线用户群聊

在某一客户端窗口中直接输入消息内容，默认为向在线用户群发送消息，该消息对服务端不可见。

例如，用户Tom发送群聊消息``Hello world``，Tom将看到如下图所示的本地提示：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/4.png" alt="image-20220703002133394" style="zoom:50%;" />

同时，所有在线用户将收到Tom的群聊信息。下图为用户Vera与Suzy的程序运行界面：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/5.png" alt="image-20220703002315533" style="zoom:50%;" />

#### 用户与服务器对话

- 服务器向某一用户单独发送系统消息：

  在服务器窗口下，按照``@Specific  聊天对象昵称  信息内容``的格式输入命令，可向目标对象发送系统消息：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/6.png" alt="image-20220703002646490" style="zoom:50%;" />

​	用户将收到系统消息提示：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/7.png" alt="image-20220703002908430" style="zoom:50%;" />

- 用户向服务器发送私聊消息：

  在任一客户端窗口下，按照``@System 消息内容``格式输入命令，可向系统发送私聊消息：

  如图，在用户Suzy的窗口下向系统发送``Hello System``	消息：

  <img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/8.png" alt="image-20220703003037982" style="zoom:50%;" />

​		系统将收到如下消息提示：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/9.png" alt="image-20220703003206932" style="zoom:50%;" />

​	此条消息对其余用户不可见。

#### 服务器广播

在服务器窗口中按照``@SystemBroadcast 消息内容``格式输入命令，可对所有在线客户端进行广播。

> 若在服务端中直接输入消息内容，将默认通过服务器广播功能向所有用户发送系统消息。

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/10.png" alt="image-20220703003518205" style="zoom:50%;" />

所有在线用户均收到了系统广播内容：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/11.png" alt="image-20220703003820365" style="zoom:50%;" />

#### 文件传输

在任一客户端下，输入``@file 目标用户昵称``，可选择文件，并向目标用户传输。

例如，在Vera聊天窗口下输入``@file Suzy``,在弹出的文件窗口中选择目标文件``User/vera/helloworld.c``，可向用户Suzy发送该文件（默认接收路径为桌面）。

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/12.jpg" alt="image-20220703004717009" style="zoom:50%;" />

Vera窗口下的发送提示如下图：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/13.png" alt="image-20220703004102108" style="zoom:50%;" />

Suzy窗口下的接收提示如下图：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/14.png" alt="image-20220703004553763" style="zoom:50%;" />

文件出现在Suzy的桌面，传输成功：

<img src="https://github.com/VeraMayLin/Socket_ChattingRoom/blob/main/images/15.png" alt="image-20220703004846628" style="zoom:67%;" />
