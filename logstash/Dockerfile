FROM docker.elastic.co/logstash/logstash:8.15.3

RUN rm -f /usr/share/logstash/pipeline/logstash.conf
# Copy the PostgreSQL JDBC driver to the external library folder of Logstash
COPY bin/ /usr/share/logstash/logstash-core/lib/jars/

# Copy configuration files
COPY pipeline/ /usr/share/logstash/pipeline/
COPY config/ /usr/share/logstash/config/

# Use pipelines.yml to load multiple configs
#CMD ["logstash", "-f", "/usr/share/logstash/config/preregister.conf"]
