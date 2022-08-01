package BotZap;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class MainBot {
    public static void waitForOpenZap(WebDriver driver){
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("side")));
        } catch (TimeoutException e){
            System.out.println("O elemento nao foi encontrado a tempo");
            driver.quit();
        }
    }

    public static void selecionaHost(WebDriver driver){
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='zoWT4'] span[title='Pedros']")));
            WebElement ricardo = driver.findElement(By.cssSelector("div[class='zoWT4'] span[title='Pedros']"));
            ricardo.click();
        } catch (TimeoutException e){
            System.out.println("Nao achei o ricardao");
            driver.quit();
        }
    }

   public static void novaMensagem(WebDriver driver){
       try {
           WebDriverWait wait = new WebDriverWait(driver, Duration.ofMinutes(30));
           wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@aria-label='1 mensagem não lida']")));
           driver.findElement(By.xpath("//span[@aria-label='1 mensagem não lida']")).click();
           pegaMensagem(driver);
       } catch (TimeoutException e){
           System.out.println("Nenhuma mensagem foi encontrada");
           driver.quit();
       }
   }

    // FUNCAO DE MANDAR MENSAGEM -> RECEBE DRIVER E UMA STRING COM A MENSAGEM A SER PASSADA
    public static void mandaMensagem(WebDriver driver, String mensagem){
        //SELECIONA A CAIXA DE TEXTO PARA ESCREVER A MENSAGEM
        WebElement caixaDeTexto = driver.findElement(By.xpath("//p[@class='selectable-text copyable-text']"));
        caixaDeTexto.sendKeys(mensagem);

        // SELECIONA O ÍCONE DE ENVIAR MENSAGEM E MANDA MENSAGEM
        WebElement envia = driver.findElement(By.xpath("//span[@data-testid = 'send']"));
        envia.click();
    }

    public static void pegaMensagem(WebDriver driver){
        WebElement painelMensagens = driver.findElement(By.className("_3K4-L"));
        try {
            List<WebElement> lista = painelMensagens.findElements(By.xpath("//div[contains(@class,'message-in')]"));
            String txt = lista.get(lista.size() - 1).findElement(By.cssSelector("span.selectable-text")).getText();
            System.out.println("Texto: " + txt);
            selecionaHost(driver);
            mandaMensagem(driver, txt);
        } catch (NoSuchElementException e){
            System.out.println("nao achou tonto");
            driver.quit();
        }
    }


    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/");
        waitForOpenZap(driver);

        try {
            while (true) {
                novaMensagem(driver);
            }
        } catch (){}

    }

}
