package io.zooobserver

import io.zooobserver.annotation.AllOpen
import io.zooobserver.job.ThreadScanningJob
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@AllOpen
@SpringBootApplication
@EnableJpaRepositories
@EntityScan
@EnableTransactionManagement
@PropertySource("classpath:global.properties")
@EnableScheduling
class Application(val threadScanningJob: ThreadScanningJob) : InitializingBean {

    override fun afterPropertiesSet() {
        threadScanningJob.execute() //fire once
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }
}