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

(defn -main
  "Handles command line arguments. Can either run a test, or a web server for
  browsing results."
  [& args]
  (prn "Hello, world!" args))
