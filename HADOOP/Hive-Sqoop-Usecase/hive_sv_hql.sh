#!/usr/bin/env bash
hive -e " SELECT survey_id , AVG ( rating) FROM ( SELECT survey_id, rating, COUNT (user_id) OVER (PARTITION BY survey_id) AS num_users FROM bank.survey_analysis )inne WHERE  num_users >= 10 GROUP BY survey_id HAVING AVG(rating) < 3";
