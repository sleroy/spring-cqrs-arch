---
layout: page
title: Introduction
permalink: /introduction/
---
# To conceive an application using CQRS, you need to think that way :

*  *What are my use cases* : define your use-cases expressed as operation s(READ or Write) or eventually a sequence flow diagram per use-case
  
*  *Distinguish your write access* : for example create a new user, edit his phone number. Avoid as much as possible generic and poor business meaning operations as CRUD (Create, Update, Delete). Think about what are you trying to update ? His personal details ? Are you toggling the email configuration flag ? Obviously all operations are a write and could be written as a big Update method. Cqrs is enforcing the DDD approach. Meaning is a rule to write better application toward functional design rather than technical design and to increase the productivity.
  
*  *Start by writing your commands* : that is the easiest part

