./clean.sh
echo "*** Building"
pushd frontend/
npm run build
popd
pushd backend/
ln -s "$(readlink -f ../frontend/build/)" src/main/resources/static
./mvnw package
echo "========================================================================"
echo "Output WAR file:"
echo "  $(readlink -f target/*.war)"
echo "========================================================================"
popd
