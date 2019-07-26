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
            [jepsen.os.debian :as debian]
            [slingshot.slingshot :refer [try+]]))

(def dir "/opt/elastos")
(def binary "ela")
(def cli-binary "ela-cli")
(def logfile (str dir "/ela.log"))
(def pidfile (str dir "/ela.pid"))

(defn ela-node
  "Ela node for a particular version."
  [version]
  (reify
   db/DB
   (setup! [_ test node]
           (c/su
            (info node "installing ela" version)
            (let [url (str "http://172.16.0.120/ela.zip")]
              (cu/install-archive! url dir))

            (cu/start-daemon!
             {:logfile logfile
              :pidfile pidfile
              :chdir   dir}
             binary)
            )
           )

   (teardown! [_ test node]
              (info node "tearing down ela")
              (cu/stop-daemon! binary pidfile)
              (c/su
               (c/exec :rm :-rf dir))
              )))


(defn ela-test
  "Given an options map from the command-line runner (e.g. :nodes, :ssh,
  :concurrency, ...), constructs a test map."
  [opts]
  (merge tests/noop-test
         opts
         {:name "ela"
          :os   debian/os
          :db   (ela-node "v0.3.5")}))

(defn -main
  ""
  [& args]
  (cli/run! (cli/single-test-cmd {:test-fn ela-test})
            args))
