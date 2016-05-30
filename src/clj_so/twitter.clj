(ns clj-so.twitter
  (:require [environ.core :refer [env]]
            [clj-so.so]
            [twitter.oauth :refer [make-oauth-creds]]
            [twitter.callbacks]
            [twitter.callbacks.handlers]
            [twitter.api.restful :refer :all])
  (:import (twitter.callbacks.protocols SyncSingleCallback)))

(def my-creds (make-oauth-creds (env :app-consumer-key)
                                (env :app-consumer-secret)
                                (env :user-access-token)
                                (env :user-access-token-secret)))

(defn tweet
  "Publish clojure questions in stackoverflow"
  [questions]
  (loop [qs questions
         published #{}]
    (when-let [q (first qs)]
      (when-not (contains? published (:question-id q)) 
        #_(statuses-update :oauth-creds my-creds
                           :params {:status (:content q)})
        (println "tweeted: " (:question-id q) (:content q)))
      (recur (rest qs)
             (conj published (:question-id q))))))
