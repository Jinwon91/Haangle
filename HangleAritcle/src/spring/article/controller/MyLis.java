package spring.article.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

/**
 * Application Lifecycle Listener implementation class MyLis
 *
 */
@WebListener
public class MyLis extends ContextLoaderListener implements ServletContextListener {
}
