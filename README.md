                        
Name: Sunny Yang 

contact: ggyang2018@gmail.com

License: GPL as open source  


BICA (Booking Invoice Confirmation App) is rich client, stand-alone Java/Swing desktop toolkit, simple to user (5 minutes training) for non-computer person and zero management. The Details description is on the BICA_Description.pdf file within the project. Here is only a summary in following sections. The details include pictures can be seen the document: BICA_Description.pdf.  

Liberton Kirk uses SKEDDA's web application to book event activities of kirk halls. A kirk staff collects booking information from a client, and submit to SKEDDA. A booking form could be a single booking, multiple bookings,  or pre-booking of whole year. Later on the SKEDDA can output an excel file in the terms of chosen time period, which contains all clients' booking information in the period.    

A kirk staff creates a booking confirmation as contract to indicate hall, time, requirement, payments, legal reliability etc, for both sides sing off; and generates invoice sheet contains bank, payment, etc.. to client. It used to use the word template to fill information, which is time consuming, non insistency, and missing items. Liberton Kirk manage team did a good job of standardization on both output formatting and procedures. BICA (Booking Invoice Conformation App) implemented the job. It is a standalone single user desktop application and promote efficiency, automation, and standardisation.

BICA takes an excel file of data from the SKEDDA as input, regroup and processing, then produce both booking contract and invoice in PDF format as output. It has following features.
  1. using apache poi package to parse excel files and transform to java data objects.
  2. group these data object according the combination of event title and client name.
  3. using apache pdfbox to produce pdf files for both booking contract and invoice.
  4. The data to create pdf files will stored in an embedded database (hsqldb)
  5. Statistically parse the excel data file and generated pie chart to indicate groups proposition in terms of income and duration using jfreechart library.
  6. A interactive GUI based on javax.swing makes these operations feasible and visible.
  7. Apache log4j framework is for logging.

BICA was written in Java,  IDE is IntelliJ IDEA, and build tool is gradle with kotlin DSL

Future improvement would be: 
  1. Tab indexing and menu item interaction: menu item enable/disable according to current tab choice. 
	2. Data field configuration: date field was hard coding, it may be put into property for flexibility.  
	3. Better looking and feel: 
	4. Logging refine: current logging is coarse only for system error. It may be refined into operations 
