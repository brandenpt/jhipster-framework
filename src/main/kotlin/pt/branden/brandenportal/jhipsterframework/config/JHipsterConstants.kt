/*
 * Copyright 2016-2019 the original author or authors from the JHipster project.
 *
 * Modifications Copyright (c) 2020 Branden(r) Lda. Developed by Fernando Rodrigues.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.branden.brandenportal.jhipsterframework.config

/**
 * JHipster constants.
 */
object JHipsterConstants {

    // Spring profiles for development, test and production, see https://www.jhipster.tech/profiles/
    /** Constant `SPRING_PROFILE_DEVELOPMENT="dev"`  */
    const val SPRING_PROFILE_DEVELOPMENT = "dev"
    /** Constant `SPRING_PROFILE_TEST="test"`  */
    const val SPRING_PROFILE_TEST = "test"
    /** Constant `SPRING_PROFILE_PRODUCTION="prod"`  */
    const val SPRING_PROFILE_PRODUCTION = "prod"
    /** Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
     * Constant `SPRING_PROFILE_CLOUD="cloud"`  */
    const val SPRING_PROFILE_CLOUD = "cloud"
    /** Spring profile used when deploying to Heroku
     * Constant `SPRING_PROFILE_HEROKU="heroku"`  */
    const val SPRING_PROFILE_HEROKU = "heroku"
    /** Spring profile used when deploying to Amazon ECS
     * Constant `SPRING_PROFILE_AWS_ECS="aws-ecs"`  */
    const val SPRING_PROFILE_AWS_ECS = "aws-ecs"
    /** Spring profile used to enable swagger
     * Constant `SPRING_PROFILE_SWAGGER="swagger"`  */
    const val SPRING_PROFILE_SWAGGER = "swagger"
    /** Spring profile used to disable running liquibase
     * Constant `SPRING_PROFILE_NO_LIQUIBASE="no-liquibase"`  */
    const val SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase"
    /** Spring profile used when deploying to Kubernetes and OpenShift
     * Constant `SPRING_PROFILE_K8S="k8s"`  */
    const val SPRING_PROFILE_K8S = "k8s"
}
