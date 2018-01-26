(ns clj-so.aws-lambda
  (:require
   [clojure.walk :refer [keywordize-keys]]
   [amazonica.aws.s3 :as s3]
   [clj-time.format :as f]))

(def custom-formatter (f/formatter "yyyy-MM-dd'T'kk:mm:ss'Z'"))

(defn get-from-time
  []
  (-> (s3/get-object "clojureatso" "last-update")
      :input-stream
      slurp))

(defn put-to-time
  [t]
  (s3/put-object "clojureatso" "last-update" t))

(defn -publish
  [this e]
  (let [event (->> e (into {}) keywordize-keys)
        from (->> (get-from-time) (f/parse custom-formatter))
        to   (->> (:time event)   (f/parse custom-formatter))]
    #_(->> (get-clj-questions from to)
           (map so-question->tweet)
           tweet)
    (put-to-time (:time event))
    (str "Tweet Clj SO: " from " - " to)))

;; unfortunately aws could not pass json as string 
(gen-class
 :name "clj-so.aws-lambda.TweetPublisher"
 :methods [[publish [java.util.Map] String]])
