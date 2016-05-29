(defproject clj-so "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.11.0"]
                 [environ "1.0.3"]
                 [clj-http "2.2.0"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [twitter-api "0.7.8"]]
  :plugins [[lein-environ "1.0.3"]]
  :main ^:skip-aot clj-so.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:env {:app-consumer-key "Cm89dBaFy7UCrIzFWAeDCeKiR"
                         :app-consumer-secret "vN1xPcx0mqDYIyc7bPNEVhWshDlqBQ11nKYJffRpdKschyEhnW"
                         :user-access-token "736482239630872576-PlUPL84vAbvT2ox30IzYjnrbKW2k7x6"
                         :user-access-token-secret "5oHuxb4CAlBfLOsvibjXcZFHpshHVG3eayvt8x0KHKx6A"}}})
