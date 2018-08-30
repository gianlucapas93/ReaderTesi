# ReaderTesi
- op.initializeOnOff(): a partire dall'indice del file d'ingresso, crea il file con tutti gli attributi (uno per riga) e
inizializza a 1 il valore on/off. Se l'attributo è indesiderato, bisogna riportare il valore a 0
- op.createNewOnOff(): ricopia in un nuovo file tutti gli attributi a 1 del file precedente
- op.initializeDiscretization(): prende gli attributi dal file precedente (quindi solo quelli che sono a 1) e crea un
nuovo file in cui verranno inserite le varie preferenze per la discretizzazione.


- readChooseFile(): carica in un vettore il mapping tra l'attributo e 1/0


- clean(): elimina gli attributi indesiderati dal file 2016 (attributi separati da ",")
- clean2015(): elimina gli attributi indesiderati dal file 2016 (attributi separati da ";")

N.B. Il file 2015 e il file 2016 hanno una formattazione diversa (attributi 2015 separati da ";" quelli 2016 separati da ",")
quindi per il primo passaggio di cancellazione degli attributi non desiderati viene usata una funzione diversa.
Dopo la clean(), gli attributi dei file vengono separati entrambi da "," quindi non c'è bisogno di differenziare

- propagateAccSx(): il valore di acc_sx è diverso da 0 (riporta la data dell'incidente) solo nella riga relativa a quel giorno,
tramite questa funzione, il valore diverso da 0 viene propagato a tutta la settimana dell'incidente

- groupByWeek(): raggruppa per settimana (1 polizza = max 52 righe)
- groupByMonth(): raggruppa per mese (1 polizza = max 12 righe)
- groupByYear(): raggruppa per anno (1 polizza = 1 riga)

- doDiscretization(): discretizza il file di input secondo la mappa caricata dalla readOnOff() che prende in input il file di discretizzazione
- splitOldNewAnnSem(): divide il file integrale in 4 file: polizze NuoveAnnuali, NuoveSemestrali, VecchieAnnuali, VecchieSemestrali
- splitForNplza(): crea un file per ogni polizza
