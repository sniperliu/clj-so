(ns clj-so.core
  (:require [clj-so.so :refer :all]
            [clj-so.twitter :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(def last-so-tweet-time ".last_so_tweet")

(def custom-formatter (f/formatter "yyyyMMdd kk:mm:ss"))

(defn spit-now []
  (let [n (t/now)]
    (spit last-so-tweet-time (f/unparse custom-formatter n))
    n))

(defn get-so-tweet-from-time []
  (if-not (.exists (java.io.File. last-so-tweet-time))
    (spit-now)
    (f/parse custom-formatter (slurp last-so-tweet-time))))

(defn -main [& args]
  (let [from (get-so-tweet-from-time)
        to (spit-now)]
    (->> (get-clj-questions from to)
         (map so-question->tweet)
         tweet)))
