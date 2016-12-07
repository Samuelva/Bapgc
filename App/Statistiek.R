myAdd=function()
{
  setwd("~/School/Jaar 4/Bapgc/")
  cijfers <- read.csv("Test.csv", sep=";", header=T, stringsAsFactors = FALSE)
  
  cijfers$Totaal <- NA
  totaal <- c()
  for (x in c(1:length(cijfers$Studentnr)))
  {
    tot <- sum(cijfers[x,3:12])
    totaal <- c(totaal, tot)
  }
  cijfers$Totaal <- totaal
  
  cijfers$Cijfer <- NA
  cijfer <- c()
  for (x in c(1:length(cijfers$Studentnr)))
  {
    getal <- round(cijfers$Totaal[x]/cijfers$Totaal[1]*10, digits=1)
    cijfer <- c(cijfer, getal)
  }
  cijfers$Cijfer <- cijfer
  
  #jpeg('Rplot.jpg')
  
  #dev.off()
}

boxplotVraag<-function(x) 
{
  setwd("~/School/Jaar 4/Bapgc/")
  cijfers <- read.csv("Test.csv", sep=";", header=T, stringsAsFactors = FALSE)
  
  index <- paste("Vraag", x, sep="")
  
  jpeg('Vraagplot.jpg')
  
  eval(parse(text = paste("boxplot(cijfers$", index, ", main='Resultaten ", 
                          paste(substr(index,1,nchar(index)-1), substr(index,nchar(index),nchar(index))),  
                          "')", sep="")))
  eval(parse(text = paste("legend('bottomleft', 'Totaal aantal punten: ", 
                          eval(parse(text = paste("cijfers$", index, '[1]', sep=""))), "', 
                          inset=c(-0.2,-0.3), bty='n')", sep="")))


  dev.off()
}

# eval(parse(text = paste("legend('bottomleft', 'Totaal aantal punten: ", 
#                         eval(parse(text = paste("cijfers$", index, '[1]', sep=""))), "')")))

histogram <- function(x)
{
  setwd("~/School/Jaar 4/Bapgc/")
  cijfers <- read.csv("Test.csv", sep=";", header=T, stringsAsFactors = FALSE)
  
  jpeg('Histogram.jpg')
  
  eval(parse(text = paste('hist(cijfers$', x, ', breaks=seq(0,5), col=c("firebrick1", "royalblue", 
                          "lawngreen", "mediumorchid4", "yellow2"), main = "Deelnemers bij ', x, '", 
                          xlab="', x, '", ylab="Aantal")', sep="")))
  
  dev.off()
}
