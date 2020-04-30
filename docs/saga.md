## How to make an command transactional ?

Transaction can be implemented in two ways :

* Creating an ICommandExecutionListener and trigger the transaction as a wrapper over 
  the execution of a transaction
* Directly on the CommandHandler by adding the appropriate @Transaction
