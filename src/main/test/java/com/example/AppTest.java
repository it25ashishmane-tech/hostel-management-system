package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {

    @Test
    public void testHostelManagementSystem() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new ChromeDriver(options);

        try {

            driver.get("http://localhost:8081");

            assertEquals(
                    "Hostel Management System",
                    driver.getTitle()
            );

            String heading =
                    driver.findElement(By.tagName("h1"))
                    .getText();

            assertEquals(
                    "Hostel Management System",
                    heading
            );

            driver.findElement(By.id("studentName"))
                    .sendKeys("Rahul");

            driver.findElement(By.id("roomNumber"))
                    .sendKeys("101");

            driver.findElement(By.id("course"))
                    .sendKeys("IT");

            driver.findElement(By.tagName("button"))
                    .click();

            String message =
                    driver.findElement(By.id("message"))
                    .getText();

            assertTrue(
                    message.contains(
                            "Room 101 allocated to Rahul"
                    )
            );

        } finally {

            driver.quit();
        }
    }
}