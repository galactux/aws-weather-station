echo "*** Cleaning"
pushd backend/
rm -rf src/main/resources/static
rm -f target/*.war
./mvnw clean
popd
rm -rf frontend/build/
