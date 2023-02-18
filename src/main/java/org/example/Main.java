package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Thread;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        String searchedColumn = "3PA";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter player name: ");
        String name = scanner.nextLine();
        scanner.close();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().minimize();

        driver.navigate().to("https://www.nba.com/stats");
        WebElement search = driver.findElement(By.className("StatsSearch_input__DzZ3G"));
        search.sendKeys(name);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        List<WebElement> nameList = driver.findElements(By.className("StatsSearchSuggestion_link__ZGLjJ"));
        if(nameList.size() == 0) {
            System.out.println("No players found.");
            driver.close();
            return;
        }
        String found = nameList.get(0).getText();

        nameList.get(0).click();

        List<WebElement> tables = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driv -> driv.findElements(By.className("Crom_table__p1iZz")));

        List<WebElement> headRows = tables.get(0).findElements(By.tagName("tr"));

        List<WebElement> head = headRows.get(1).findElements(By.tagName("th"));

        WebElement tbody = tables.get(0).findElement(By.className("Crom_body__UYOcU"));
        List<WebElement> bodyRows = tbody.findElements(By.tagName("tr"));

        int dataCol = 0;
        for(int i = 0; i < head.size(); i++) {
            WebElement column = head.get(i);
            String colName = column.getText();

            if(colName.equals(searchedColumn)) {
                dataCol = i;
                break;
            }
        }

        ArrayList<String> data = new ArrayList<String>();

        for(WebElement row : bodyRows) {

            List<WebElement> columns = row.findElements(By.tagName("td"));
            data.add(columns.get(0).getText() + " " + searchedColumn + ": " + columns.get(dataCol).getText());

            //System.out.println(columns.get(0).getText() + " " + searchedColumn + ": " + columns.get(dataCol).getText());
        }

        driver.close();

        System.out.println("Found for player: " + found);
        System.out.println("Searched for yearly " + searchedColumn + " data");
        for(String line : data){
            System.out.println(line);
        }
    }

}