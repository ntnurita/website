# should be plated in tomcat6/webapps/oai/WEB-INF/classes (within the jOAI webapp)
# will update the classes from the metadata-artifact output directory

rm -Rf edu
cp -R <artifact-output-dir>/edu .
chown tomcat6:tomcat6 -R edu
