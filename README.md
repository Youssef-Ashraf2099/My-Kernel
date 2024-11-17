## Description
My Kernel is a custom-built operating system kernel designed to explore and understand the inner workings of operating systems. This project is a hands-on implementation focused on key concepts such as memory management, process scheduling, and hardware communication. 

### Features
- Basic process management
- Simple memory management
- System calls implementation
- Kernel module support

### Prerequisites
Make sure you have the following installed before running the project:
- [JDK 21](https://openjdk.java.net/)

  
  ### Steps for those who want to contribute
1. Clone the repository:
    ```bash
    git clone https://github.com/Youssef-Ashraf2099/my-Kernel.git
    ```

2. Navigate to the project folder:
    ```bash
    cd my-Kernel
    ```

3. Build the kernel:
    ```bash
    make
    ```

4. To test the kernel in QEMU:
    ```bash
    make run
    ```

    Or to test with VirtualBox, follow the setup for VirtualBox in the `docs` folder.
