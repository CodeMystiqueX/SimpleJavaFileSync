SimpleJavaFileSync
一个简单而高效的文件同步工具，使用 Java 编写。该工具可以将文件从源目录同步到目标目录，仅复制修改时间更新的文件，非常适合用于备份或跨目录/服务器保持文件一致性。

目录
功能特点
运行要求
使用方法
工作原理
如何贡献
许可证
功能特点
高效同步：仅在源文件比目标文件更新时进行复制，基于文件的最后修改时间。
目录结构保持：在目标目录中保留与源目录相同的层次结构。
跨平台支持：借助 Java 的可移植性，支持 Windows、Linux 和 macOS。
错误处理：优雅地处理无法访问的文件和缺失的目录。
轻量级：无额外依赖，纯 Java 实现，使用 NIO.2。
运行要求
Java 8 或更高版本：确保已安装 Java 运行环境 (JRE) 或开发工具包 (JDK)。
操作系统：兼容 Windows、Linux 或 macOS。
检查 Java 版本的命令：

<BASH>
java -version
使用方法
下载或构建 JAR 文件：
从 Releases 页面下载预构建的 JAR 文件（如果可用）。
或者克隆此仓库并使用 Maven 构建：
<BASH>
git clone https://github.com/yourusername/SimpleJavaFileSync.git
cd SimpleJavaFileSync
mvn package
构建后的 JAR 文件将位于 target/SimpleJavaFileSync-1.0-SNAPSHOT.jar。
运行工具： 使用以下命令将文件从源目录同步到目标目录：
<BASH>
java -jar SimpleJavaFileSync-1.0-SNAPSHOT.jar <sourceDir> <targetDir>
将 <sourceDir> 替换为要同步的源目录路径。
将 <targetDir> 替换为要同步到的目标目录路径。 示例：
<BASH>
java -jar SimpleJavaFileSync-1.0-SNAPSHOT.jar /home/user/docs /mnt/backup
输出信息：
工具会为每个同步的文件打印信息：
<TEXT>
Synced: /home/user/docs/file1.txt -> /mnt/backup/file1.txt
如有错误，会显示类似信息：
<TEXT>
Failed to access file: /home/user/docs/restricted.txt (Permission denied)
工作原理
SimpleJavaFileSync 利用 Java 的 NIO.2（java.nio.file）包来进行现代化的文件操作。主要功能包括：

使用 Files.walkFileTree 递归遍历源目录。
计算相对路径以在目标目录中保持相同的目录结构。
比较源文件和目标文件的最后修改时间，避免不必要的复制。
根据需要在目标目录中创建缺失的文件夹。
覆盖目标目录中过时的文件。
注意：目前工具不会删除目标目录中已被源目录删除的文件，此功能可能在未来更新中添加。

如何贡献
欢迎贡献！请按照以下步骤操作：

Fork 此仓库。
为您的功能或错误修复创建一个新分支。
使用描述性的消息提交您的更改。
推送您的分支并创建 Pull Request。
如有 bug 或功能请求，欢迎在 Issue 中提出。

许可证
本项目采用 MIT 许可证 - 详情请见 LICENSE 文件。
