.. _setup:

================
HAPI pheno Setup 
================


To use this software, you will need to target a FHIR server 
that incorporates the Phenopackets Implementation guide.

For testing purposes, we have developed a separate Java project that 
starts a FHIR server locally:
`hapi-pheno-server-setup <https://github.com/phenopackets/hapi-pheno-server-setup>`_. 


To use this package, clone the code and enter the 
following commands to start the server (the code only needs 
to be cloned the first time).


.. code-block:: bash
   :caption: setup

       git clone https://github.com/phenopackets/hapi-pheno-server-setup
       cd hapi-pheno-server-setup
       bin/start.sh 


The last command depends on a running 
`Docker <https://www.docker.com/>`_ demon. The API of Docker 
has changed frequently in the last several releases. We have 
tested the code using Docker version 24.0.4, build 3713ee1
on Ubuntu. 

If you get error messages, it is likely that there is a Docker version 
incompatiblity, and we recommend that you update your Docker version.


LOINC data
^^^^^^^^^^

todo. 


