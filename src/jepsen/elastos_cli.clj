(ns jepsen.elastos-cli
  (:require [clojure.tools.logging :refer :all]
            [clojure.string :as str]
            [verschlimmbesserung.core :as v]
            [jepsen.cli :as cli]
            [jepsen.client :as client]
            [jepsen.control :as c]
            [jepsen.db :as db]
            [jepsen.generator :as gen]
            [jepsen.tests :as tests]
            [jepsen.control.util :as cu]
            [jepsen.os.ubuntu :as ubuntu]
            [slingshot.slingshot :refer [try+]]))

(def dir "/opt/elastos")
(def logdir "/root")
(def binary "ela")
(def cli-binary "ela-cli")
(def logfile (str logdir "/ela.log"))
(def pidfile (str dir "/ela.pid"))

(defn ela-node
  "Ela node for a particular version."
  [version]
  (reify
   db/DB
   (setup! [_ test node]
           (c/su
            (info node "installing ela" version)
            ;todo replace http://172.16.0.120/ela.zip with real download address
            (let [url (str "http://172.16.0.120/ela.zip")]
              (cu/install-archive! url dir))

            (cu/start-daemon!
             {:logfile logfile
              :pidfile pidfile
              :chdir   dir}
             binary
             ; todo replace dns with real dns server
             :-dns "http://172.16.0.120:20338"
             ; todo set magic with rules
             :-magic 2019007)

            ; wait for node initializing
            (Thread/sleep 10000)))

   (teardown! [_ test node]
              (info node "tearing down ela")
              (cu/stop-daemon! binary pidfile)
              (c/su
               (c/exec :rm :-rf dir)))

   db/LogFiles
   (log-files [_ test node]
              [logfile])))


(defn ela-test
  "Given an options map from the command-line runner (e.g. :nodes, :ssh,
  :concurrency, ...), constructs a test map."
  [opts]
  (merge tests/noop-test
         opts
         {:name "ela"
          :os   ubuntu/os
          :db   (ela-node "v0.3.5")}))

(defn -main
  ""
  [& args]
  (cli/run! (cli/single-test-cmd {:test-fn ela-test})
            args))
