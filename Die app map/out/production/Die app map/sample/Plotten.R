dataframe<-function() 
{
  # Lees bestand in, uiteindelijk wordt dit bestand meegegeven door java
  setwd("~/School/Jaar 4/Bapgc/")
  numbers <- read.csv("brela_1e_1617.csv", sep=";", header=T, stringsAsFactors = FALSE)
  
  # Verzamel alle kolomnamen van kolommen die opdrachten bevatten bevatten
  questions <- c()
  for (x in c(1:ncol(numbers)))
  {
    if (colnames(numbers[x]) == "Opgave")
    {
      questions <- c(questions, colnames(numbers[x]))
    }
    if (substr(colnames(numbers)[x],1,1) == "X")
    {
      questions <- c(questions, colnames(numbers[x]))
    }
  }
  
  # Zet de opgaven en deelvragen in twee aparte lijsten
  questionlist <- c()
  subquestionlist <- c()
  for (x in c(1:length(questions)))
  {
    eval(parse(text = paste("question <- numbers$", questions[x], "[1]", sep="")))
    questionlist <- c(questionlist, question)
    eval(parse(text = paste("subquestion <- numbers$", questions[x], "[2]", sep="")))
    subquestionlist <- c(subquestionlist, subquestion)
  }
  
  # Er wordt door de twee lijsten heen geloopt. 
  # Voor 'questionlist' geldt: als er een NA is, krijgt het de waarde van de positie daarvoor in de lijst
  # Voor 'subquestionlist' geldt: als er een NA is, krijgt het de waarde van 1
  # Daarna worden de kolomnamen in het data frame 'numbers' verandert in de waarden van de vragen.
  # Dit houdt in dat in de kolom, de eerste van tweede rij de naam van die kolom vormen. 
  for (x in c(1:length(questionlist)))
  {
    if (is.na(questionlist[x])==TRUE)
    {
      questionlist[x] <- questionlist[x-1]
    }
    if(is.na(subquestionlist[x])==TRUE)
    {
      subquestionlist[x] <- 1
    }
    eval(parse(text = paste("names(numbers)[names(numbers)=='", questions[x], "'] <-", 
                            paste(questionlist[x], subquestionlist[x], sep="."), sep="")))
  }
  numbers$EC[4:nrow(numbers)] <- sample(40:60, nrow(numbers)-3, replace=T)
  return(numbers)
}

boxplotVraag<-function(x) 
{
  numbers <- dataframe()
  #numbers <- y
  index <- paste(x)
  
  jpeg(filename = "Boxplot.jpg", width = 200, height = 300, units = "px")
  
  eval(parse(text = paste("boxplot(numbers$`", index, "`[4:length(numbers$Studentnummer)], main='Resultaten ", 
                          paste(substr(index,1,nchar(index)-1), substr(index,nchar(index),nchar(index))),  
                          "')", sep="")))

  
  par(fig = c(0, 1, 0, 1), oma = c(0, 0, 0, 0), mar = c(0, 0, 0, 0), new = TRUE)
  plot(0, 0, type = "n", bty = "n", xaxt = "n", yaxt = "n")
  eval(parse(text = paste("legend('bottomleft', 'Totaal aantal punten: ", 
                          eval(parse(text = paste("numbers$`", index, '`[3]', sep=""))), "', bty='n')", 
                          sep="")))
  
  
  dev.off()
}

histogram <- function(x)
{
  numbers <- dataframe()
  jpeg('Histogram.jpg', width = 500, height = 300, units = "px")
  
  if (x == "cijfer")
  {
    eval(parse(text = paste('tmp <- hist(numbers$', x, '[4:length(numbers$Studentnummer)], breaks=c(0:10),
                          main = "Deelnemers bij ', x, '", xlab="', x, '", ylab="Aantal", xaxt="n")', 
                            sep="")))
    axis(1, at=tmp$mids, labels=0:9)
  }
  
  #if (x == "voldoende")
  
  dev.off()
}

lijndiagram <- function()
{
  numbers <- dataframe()
  amountOfEC <- c()
  EC <- numbers$EC[4:nrow(numbers)]
  for(x in c(0:60))
  {
    amount <- c()
    for (y in c(1:length(EC)))
    {
      if (x < EC[y] || x == EC[y])
      {
        amount <- c(amount, EC[y])
      }
    }
    percentage <- length(amount)/length(EC)*100
    amountOfEC <- c(amountOfEC, percentage)
  }
  jpeg('Lijndiagram.jpg', width = 500, height = 300, units = "px")
  plot(amountOfEC, type="l", col="firebrick1", main="Aantal EC voor jaar 2016", xlab="Aantal EC",
       ylab="Percentage")
  legend("bottomleft", "2016", fill="firebrick1", ncol =1, bty ="n", xpd=TRUE)
  dev.off()
}