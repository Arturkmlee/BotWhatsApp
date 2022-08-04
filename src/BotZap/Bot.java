package BotZap;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class Bot implements Runnable{
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
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[class='zoWT4'] span[title='TesteZap']")));
            // RICARADO É O HOST (NOME DA PESSOA/GRUPO PARA REDIRECIONAR AS MENSAGENS)
            WebElement ricardo = driver.findElement(By.cssSelector("div[class='zoWT4'] span[title='TesteZap']"));
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
       novaMensagem(driver);
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
        } catch (Exception e){
            Actions acao = new Actions(driver);
            System.out.println("Nao eh uma mensagem");

            List<WebElement> lista = painelMensagens.findElements(By.xpath("//div[contains(@class,'message-in')]"));
            acao.moveToElement(lista.get(lista.size() - 1).findElement(By.cssSelector("div[data-testid='msg-container']"))).perform();

            WebElement setinha = driver.findElement(By.xpath("//span[@data-testid='down-context']"));
            setinha.click();

            WebElement encaminhar = driver.findElement(By.cssSelector("div[aria-label='Encaminhar mensagem']"));
            encaminhar.click();

            driver.findElement(By.cssSelector("span[data-testid='forward']")).click();
            driver.findElement(By.cssSelector("div[class='_2nY6U vq6sj _2OR6D'] span[title='TesteZap']")).click();
            novaMensagem(driver);
        }
    }


    public void run(){
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\artur.lee\\Documents\\Selenium\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://web.whatsapp.com/");
        waitForOpenZap(driver);
        novaMensagem(driver);
    }

}
