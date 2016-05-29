(ns clj-so.so
  (:require [cognitect.transit :as transit]
            [clj-http.client :as client]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  (:import [java.io ByteArrayInputStream]))

#_(def in ())
#_(def reader (transit/reader in :json))

(def so-api-uri "https://api.stackexchange.com/")
(def so-api-version "2.2/")

#_(def so-sites (str so-api "/2.2/sites?page=1&pagesize=10"))

;; filter=!SmOhH2b13hqBdOssjc
(def test-json "{\"name\": \"Stack Overflow на русском\"}")
(def test-json-1 "{\"name\": \"スタック・オーバーフロー\"}")

(defn so-api
  ([endpoint] (so-api endpoint {}))
  ([endpoint params]
   (client/get (str so-api-uri so-api-version endpoint)
               {:query-params params
                :body-encoding "UTF-8"
                :as :json})))

(defmacro with-pagination->
  "Macros to provide pagination support to StackExchange apis"
  [& body]
  (let [api-call (first body)
        handler (rest body)]
    `(loop [items# (empty [])
            return?# false
            page# 1]
       (let [res# (-> (~@(butlast api-call)
                       (merge ~(last api-call) {"page" page#
                                                "paegsize" 20}))
                      ~@handler)]
         (if return?#
           items#
           (recur (concat items# (get res# "items"))
                  (not (get res# "has_more"))
                  (inc page#)))))))

(defn get-so-sites []
  (with-pagination->
    (so-api "sites" {})
    :body
    (.getBytes "UTF-8")
    (ByteArrayInputStream.)
    (transit/reader :json)
    (transit/read)))

(defn get-clj-questions [^org.joda.time.DateTime from
                         ^org.joda.time.DateTime to]
  (let [from-long (long (/ (c/to-long from) 1000))
        to-long (long (/ (c/to-long to) 1000))]
    (with-pagination->
      (so-api "questions"
              {"site" "stackoverflow"
               "tagged" "clojure"
               "fromdate" from-long
               "todate" to-long})
      :body
      (.getBytes "UTF-8")
      (ByteArrayInputStream.)
      (transit/reader :json)
      (transit/read))))

(defn so-site->tweet [site]
  (map #(hash-map "name" (get % "name")
                  "site_url" (get % "site_url")
                  "audience" (get % "audience")
                  "api_site_parameter" (get % "api_site_parameter"))
       site))

(defn so-question->tweet [q]
  (let [tags (->> (map #(str "#" %) (get q "tags"))
                  (clojure.string/join " "))
        link (get q "link")]
    (str tags "\n" link)))
