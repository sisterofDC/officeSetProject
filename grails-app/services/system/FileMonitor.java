package system;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/*]

记录文件的变更时间或者大小，间隔n次扫描没发生变化则认为写入完成。n可以自行根据文件处理的实时性要求进行调整

不对原项目更改只能这样。一般情况下来，控制文件是否写完，会采用2种方式，1、文件末尾加入特殊结束标记；2、附带一个文件写完的检验文件，带有完成校验文件的才对主文件做处理。

那个昨天监控那个找到方法了。生成文件的时候，他有一个临时文件提供判断，当下载文件成功后，其他程序会自动删除临时文件，同时解除锁定。就是tmp文件。这里是在Windows测试，还有linux上测试
 */

public class FileMonitor {
    private FileAlterationMonitor monitor;
    public FileMonitor(long interval) {
        monitor = new FileAlterationMonitor(interval);
    }

    /**
     * 给文件添加监听
     * @param path 文件路径
     * @param listener 文件监听器
     */
    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }
    public void stop() throws Exception {
        monitor.stop();
    }
    public void start() throws Exception {
        monitor.start();
    }
}
