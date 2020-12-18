package io.kimmking.rpcfx.api;

public class FilterImpl implements Filter {
    @Override
    public boolean filter(RpcfxRequest request) {
        return true;
    }

    @Override
    public Filter next() {
        return null;
    }
}
