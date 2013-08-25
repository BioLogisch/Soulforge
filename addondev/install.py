import os, os.path, sys
import urllib, zipfile
import shutil, glob, fnmatch
import subprocess, logging
import shutil
from optparse import OptionParser
    
def soulforge_main(soulforge_dir, mcp_dir):
    sys.path.append(mcp_dir)
    from runtime.updatemd5 import updatemd5
    from runtime.reobfuscate import reobfuscate
    from soulforge import decompile, apply_initial_patches, apply_soulforge_patches, recompile, unzipbtw, copyinitialreobfuscatedfiles, rezipbtw, apply_mod_patches, reset_logger, copytree
    
    print '=============================== Soulforge Setup Start ====================================='

    print 'Decompiling'
    decompile(mcp_dir, soulforge_dir)
    print 'Applying patches'
    apply_initial_patches(mcp_dir, soulforge_dir, os.path.join(mcp_dir, 'src'), True)
    os.chdir(mcp_dir)
    updatemd5(None, True, False, False)
    reset_logger()
    os.chdir(soulforge_dir) 
    apply_soulforge_patches(mcp_dir, soulforge_dir, os.path.join(mcp_dir, 'src'), True)  
    copytree(os.path.join(soulforge_dir, 'src', 'minecraft'), os.path.join(mcp_dir, 'src', 'minecraft')) 
    copytree(os.path.join(soulforge_dir, 'src', 'common'), os.path.join(mcp_dir, 'src', 'minecraft')) 
    copytree(os.path.join(soulforge_dir, 'src', 'minecraft_server'), os.path.join(mcp_dir, 'src', 'minecraft_server')) 
    copytree(os.path.join(soulforge_dir, 'src', 'common'), os.path.join(mcp_dir, 'src', 'minecraft_server')) 
    os.chdir(mcp_dir)
    print 'Recompiling'
    recompile(None, False, False)
    reset_logger()
    print 'Reobfuscating'
    reobfuscate(None, False, False, False, False, False, False)
    reset_logger()
    print 'Unzipping BTW'
    unzipbtw(soulforge_dir)
    print 'Copying Reobfuscated Files'
    copyinitialreobfuscatedfiles(soulforge_dir, mcp_dir)
    print 'Rezipping Files'
    rezipbtw(soulforge_dir)
    updatemd5(None, True, False, False)
    reset_logger()
    os.chdir(soulforge_dir) 
    copytree(os.path.join(mcp_dir, 'src'), os.path.join(mcp_dir, 'src_base')) 
    apply_mod_patches(mcp_dir, soulforge_dir, os.path.join(mcp_dir, 'src'), True)  
    print '=============================== Soulforge Setup Finished ================================='

if __name__ == '__main__':
    parser = OptionParser()
    parser.add_option('-m', '--mcp-dir',   action='store',      dest='mcp_dir',       help='Path to download/extract MCP to',         default=None )
    options, _ = parser.parse_args()
    
    soulforge_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    mcp_dir = os.path.abspath('mcp')

    if not options.mcp_dir is None:
        mcp_dir = os.path.abspath(options.mcp_dir)
    
    soulforge_main(soulforge_dir, mcp_dir)