[关于本书 · Gradle User Guide 中文版](https://link.zhihu.com/?target=https%3A//dongchuan.gitbooks.io/gradle-user-guide-/content/)

[关于本书 · Gradle 实战](https://link.zhihu.com/?target=http%3A//lippiouyang.gitbooks.io/gradle-in-action-cn/content/)

### Gradle是什么



### Gradle打包脚本

```groovy
// packaging.gradle
afterEvaluate {
    //测试环境测试签名包  未开启混淆和资源优化，相当于本地调试
    createTask(project, "AlphaDebug", "assembleAlphaDebug")
    //测试环境测试签名包
    createTask(project, "BetaDebug", "assembleBetaBetaSign")
    //正式环境正式签名包
    createTask(project, "CentaRelease", "assembleCentalineRelease")
}

static def createTask(Project project, String taskName, String lastCommand) {
    project.tasks.create("name": taskName, "type": Exec) {
        group "packaging"
        workingDir project.rootDir
        String[] command = org.gradle.internal.os.OperatingSystem.current().isWindows() ?
                ["cmd", "/c", "gradlew.bat", lastCommand]
                : ["gradlew", lastCommand]
        commandLine command
    }
}
```

### 发布Android Library到maven仓库

1. 使用uploadArchives

```groovy
// upload_nexus.gradle
apply plugin: 'maven'
def SNAPSHOT_REPOSITORY_URL = 'http://nexus.centaline.com.cn/repository/maven-snapshots/'
def RELEASE_REPOSITORY_URL = 'http://nexus.centaline.com.cn/repository/maven-releases/'
def GROUP = 'com.centanet.android'
def NEXUS_USERNAME = 'admin'
def NEXUS_PASSWORD = 'admin123'
def NEXUS_DESC = 'library for android'
afterEvaluate {
    uploadArchives {
        repositories {
            mavenDeployer {
                repository(url: RELEASE_REPOSITORY_URL) {
                    authentication(userName: NEXUS_USERNAME, password: NEXUS_PASSWORD)
                }

                snapshotRepository(url: SNAPSHOT_REPOSITORY_URL) {
                    authentication(userName: NEXUS_USERNAME, password: NEXUS_PASSWORD)
                }

                pom.project {
                    version = VERSION_NAME
                    groupId = GROUP
                    artifactId = POM_ARTIFACT_ID
                    packaging = 'aar'
                    description = NEXUS_DESC
                }
            }
        }
    }

    task androidJavadocs(type: Javadoc) {
        source = android.sourceSets.main.java.sourceFiles
    }

    task androidJavadocsJar(type: Jar) {
        archiveClassifier.convention('javadoc')
        archiveClassifier.set('javadoc')
        from androidJavadocs.destinationDir
    }

    task androidSourcesJar(type: Jar) {
        archiveClassifier.convention('sources')
        archiveClassifier.set('sources')
        from android.sourceSets.main.java.srcDirs
    }

    artifacts {
        archives androidSourcesJar
        archives androidJavadocsJar
    }
}

```

```groovy
apply from: '../upload_nexus.gradle'
android {
    sourceSets {
        main {
            java {
                include '**/*.kt'
            }
        }
    }
}
```

2. 使用maven-publish插件

```groovy
apply plugin: 'maven-publish'

afterEvaluate {
    publishing {
        task androidSourcesJar(type: Jar) {
            archiveClassifier.convention('sources')
            archiveClassifier.set('sources')
            from android.sourceSets.main.java.srcDirs
        }

        publications {
            release(MavenPublication) {
//                from components.release
                groupId = POM_GROUP_ID
                artifactId = POM_ARTIFACT_ID
                version = VR_VERSION_NAME
                // 必须有这个 否则不会上传AAR包
                afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
                // 上传源码
                artifact androidSourcesJar
            }
        }

        repositories {
            // 定义一个 maven 仓库
            maven {
                //允许使用 http
                allowInsecureProtocol = true
                // 根据 versionName 来判断仓库地址
                url = VR_VERSION_NAME.endsWith('SNAPSHOT') ? SNAPSHOT_REPOSITORY_URL : RELEASE_REPOSITORY_URL
                // 仓库用户名密码
                credentials {
                    username = NEXUS_USERNAME
                    password = NEXUS_PASSWORD
                }
            }
        }
    }
}

#nexus
SNAPSHOT_REPOSITORY_URL=http://nexus.centaline.com.cn/repository/maven-snapshots/
RELEASE_REPOSITORY_URL=http://nexus.centaline.com.cn/repository/maven-releases/
POM_GROUP_ID=com.centanet.android
POM_ARTIFACT_ID=centanet-vr
NEXUS_USERNAME=admin
NEXUS_PASSWORD=admin123
NEXUS_DESC=library for android
VR_VERSION_NAME=0.0.1-SNAPSHOT
```

