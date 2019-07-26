(defproject jepsen.elastos-cli "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :main jepsen.elastos-cli
  :jvm-opts ["-Dcom.sun.management.jmxremote"]
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [jepsen "0.1.13-SNAPSHOT"]
                 [verschlimmbesserung "0.1.3"]]
  :repl-options {:init-ns jepsen.elastos-cli})
