pushd frontend/
rm -rf build/
npm run build
popd
pushd backend/
rm -f target/*.war
rm -rf src/main/resources/static
./mvnw clean
ln -s "$(readlink -f ../frontend/build/)" src/main/resources/static
./mvnw package
echo "========================================================================"
echo "Output WAR file:"
echo "  $(readlink -f target/*.war)"
echo "========================================================================"
popd
